/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.gui.view.control.cell.TextFieldListCell;
import com.gitlab.super7ramp.croiseur.gui.view.model.ClueViewModel;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.scene.control.Labeled;
import javafx.util.StringConverter;

import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

/**
 * A clue list cell.
 * <p>
 * It is a {@link TextFieldListCell} with the following particularities:
 * <ul>
 *     <li>It displays the cell index in the cell text part, which is by default on the left;
 *     <li>It displays the clue content (a text field) in the cell graphic part, which is by
 *     default on the right;
 *     <li>It uses a custom CSS class {@code .clue-list-cell}, inheriting from {@code .list-cell},
 *     which has the additional properties:
 *     <ul>
 *         <li>{@code -index-format: [roman|arabic]}: Defines the format of the cell index
 *     </ul>
 * </ul>
 */
final class ClueListCell extends TextFieldListCell<ClueViewModel> {

    /** The ways to format the cell index. */
    private enum IndexFormat {
        ARABIC, ROMAN
    }

    /** Styleable property factory. */
    private static final StyleablePropertyFactory<ClueListCell> FACTORY =
            new StyleablePropertyFactory<>(Labeled.getClassCssMetaData());

    /** The index format CSS metadata. */
    private static final CssMetaData<ClueListCell, IndexFormat> INDEX_FORMAT_CSS_METADATA =
            FACTORY.createEnumCssMetaData(IndexFormat.class, "-index-format", s -> s.indexFormat,
                                          IndexFormat.ARABIC, false);

    /** Converter for user clue (main text). */
    private static final StringConverter<ClueViewModel> USER_CLUE_STRING_CONVERTER =
            new StringConverter<>() {
                @Override
                public String toString(final ClueViewModel model) {
                    return model.userContent();
                }

                @Override
                public ClueViewModel fromString(final String value) {
                    final ClueViewModel clueViewModel = new ClueViewModel();
                    clueViewModel.userContent(value);
                    // System content is lost but that's no big deal
                    return clueViewModel;
                }
            };

    /** Converter for system clue (prompt text). */
    private static final Function<ClueViewModel, String> SYSTEM_CLUE_STRING_CONVERTER =
            viewModel -> viewModel.systemContent().isEmpty() ?
                    ResourceBundle.getBundle(CluesPane.class.getName()).getString("clues.none") :
                    viewModel.systemContent();

    /** The index format styleable property. */
    private final StyleableObjectProperty<IndexFormat> indexFormat;

    /**
     * Constructs an instance.
     */
    ClueListCell() {
        super(USER_CLUE_STRING_CONVERTER, SYSTEM_CLUE_STRING_CONVERTER);
        indexFormat =
                new SimpleStyleableObjectProperty<>(INDEX_FORMAT_CSS_METADATA, this, "indexFormat",
                                                    IndexFormat.ARABIC);
        indexFormat.addListener(observable -> updateItem(getItem(), isEmpty()));
        getStyleClass().add("clue-list-cell");
        setStyle("-fx-content-display: right;");
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        // Necessary otherwise custom CSS property is not taken into account.
        return FACTORY.getCssMetaData();
    }

    @Override
    public void updateItem(final ClueViewModel item, final boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
        } else {
            setText(formatIndex() + ".");
        }
    }

    private String formatIndex() {
        final int index = getIndex() + 1;
        return switch (indexFormat.getValue()) {
            case ARABIC -> arabicFormat(index);
            case ROMAN -> romanFormat(index);
        };
    }

    private static String arabicFormat(final int index) {
        return Integer.toString(index);
    }

    private static String romanFormat(final int index) {
        // Not the most efficient implementation, but definitely one of the coolest.
        return "I".repeat(index)
                  .replace("IIIII", "V")
                  .replace("IIII", "IV")
                  .replace("VV", "X")
                  .replace("VIV", "IX")
                  .replace("XXXXX", "L")
                  .replace("XXXX", "XL")
                  .replace("LL", "C")
                  .replace("LXL", "XC")
                  .replace("CCCCC", "D")
                  .replace("CCCC", "CD")
                  .replace("DD", "M")
                  .replace("DCD", "CM");
    }
}