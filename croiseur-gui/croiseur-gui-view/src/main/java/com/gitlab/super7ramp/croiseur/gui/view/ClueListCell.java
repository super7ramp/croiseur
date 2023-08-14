/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.gui.view.model.ClueViewModel;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.ResourceBundle;

/**
 * A clue list cell.
 * <p>
 * It is a list cell inspired by {@link javafx.scene.control.cell.TextFieldListCell} with the
 * following particularities:
 * <ul>
 *     <li>ListCell Label is not used at all: Everything is in the graphic part;
 *     <li>It displays the cell index in a label on the left;
 *     <li>It displays the clue content in a text field in the middle;
 *     <li>It displays a button on the right, whose action is configurable;</li>
 *     <li>Its appearance can be customized via the {@code .clue-list-cell} CSS class, inheriting
 *     from {@code .text-field-list-cell}, which has the additional properties:
 *     <ul>
 *         <li>{@code -index-format: [roman|arabic]}: Defines the format of the cell index
 *     </ul>
 * </ul>
 */
final class ClueListCell extends ListCell<ClueViewModel> {

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

    /** The index format styleable property. */
    private final StyleableObjectProperty<IndexFormat> indexFormat;

    /** The hbox containing the following label, text field and button. */
    @FXML
    private HBox containerHBox;

    /** The label containing the cell index. */
    @FXML
    private Label indexLabel;

    /** The text field containing the clue. */
    @FXML
    private TextField textField;

    /** The fill button. */
    @FXML
    private Button fillButton;

    /**
     * Constructs an instance.
     */
    ClueListCell() {
        FxmlLoaderHelper.load(this, ResourceBundle.getBundle(getClass().getName()));
        indexFormat =
                new SimpleStyleableObjectProperty<>(INDEX_FORMAT_CSS_METADATA, this, "indexFormat",
                                                    IndexFormat.ARABIC);
        indexFormat.addListener(observable -> updateItem(getItem(), isEmpty()));
    }

    @Override
    public void startEdit() {
        if (!isEditable() || !getListView().isEditable()) {
            return;
        }
        super.startEdit();
        if (isEditing()) {
            textField.setPromptText(itemToPromptText());
            textField.setText(itemToText());
            textField.selectAll();
            textField.requestFocus();
        }
    }

    @Override
    public void updateItem(final ClueViewModel item, final boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            indexLabel.setText(formatIndex());
            textField.setPromptText(itemToPromptText());
            textField.setText(itemToText());
            fillButton.setVisible(isSelected());
            setGraphic(containerHBox);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setGraphic(containerHBox);
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return FACTORY.getCssMetaData();
    }

    /**
     * Initializes the control after object hierarchy has been loaded from FXML.
     */
    @FXML
    private void initialize() {
        textField.setOnAction(event -> {
            commitEdit(itemFromText());
            event.consume();
        });
        textField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
                event.consume();
            }
        });
    }

    // model <-> view conversion methods

    /**
     * Converts text field text to item model.
     *
     * @return the item model
     */
    private ClueViewModel itemFromText() {
        return new ClueViewModel(textField.getText());
    }

    /**
     * Converts item model to text.
     *
     * @return the text
     */
    private String itemToText() {
        return getItem().userContent();
    }

    /**
     * Converts the item model to prompt text.
     *
     * @return the prompt text
     */
    private String itemToPromptText() {
        final String itemSystemContent = getItem().systemContent();
        return itemSystemContent.isEmpty() ?
                ResourceBundle.getBundle(ClueListCell.class.getName()).getString("clue.none") :
                itemSystemContent;
    }

    // Index format methods

    /**
     * Formats the the cell index.
     *
     * @return the formatted cell index
     */
    private String formatIndex() {
        final int index = getIndex() + 1;
        return switch (indexFormat.getValue()) {
            case ARABIC -> arabicFormat(index);
            case ROMAN -> romanFormat(index);
        } + ".";
    }

    /**
     * Formats the given index as a String.
     *
     * @param index the index
     * @return the formatted String
     */
    private static String arabicFormat(final int index) {
        return Integer.toString(index);
    }

    /**
     * Formats the given index as a String of roman numbers.
     *
     * @param index the index
     * @return the formatted String
     */
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