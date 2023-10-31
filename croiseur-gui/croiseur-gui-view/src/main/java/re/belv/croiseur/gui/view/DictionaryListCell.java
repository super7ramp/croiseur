/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view;

import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.StringConverter;
import re.belv.croiseur.gui.view.model.DictionaryViewModel;

/**
 * A specialized {@link CheckBoxListCell} for dictionaries list view.
 */
final class DictionaryListCell extends CheckBoxListCell<DictionaryViewModel> {

    /**
     * Converts a {@link DictionaryViewModel} to a {@link String} to be used as title of a
     * {@link ListCell}.
     */
    private static final StringConverter<DictionaryViewModel>
            DICTIONARY_LIST_CELL_STRING_CONVERTER =
            new StringConverter<>() {
                @Override
                public String toString(final DictionaryViewModel dictionaryViewModel) {
                    return dictionaryViewModel.name();
                }

                @Override
                public DictionaryViewModel fromString(final String string) {
                    throw new UnsupportedOperationException("Unused");
                }
            };

    /**
     * Max width of the cell tooltip; Dictionary description can be quite long, better wrap it on
     * several lines rather than having one line of the width of the screen.
     * <p>
     * Note: It seems this value cannot be set via CSS.
     */
    private static final double TOOLTIP_MAX_WIDTH = 350.0;

    /**
     * Constructs an instance.
     */
    DictionaryListCell() {
        super(DictionaryViewModel::selectedProperty, DICTIONARY_LIST_CELL_STRING_CONVERTER);
        // Prefer ellipsis with tooltip over horizontal scroll: Horizontal scroll feels awkward
        setPrefWidth(0);
    }

    @Override
    public void updateItem(final DictionaryViewModel item, final boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setTooltip(null);
        } else {
            final Tooltip tooltip = new Tooltip(item.name() + "\n\n" + item.description());
            tooltip.setMaxWidth(TOOLTIP_MAX_WIDTH);
            tooltip.setWrapText(true);
            setTooltip(tooltip);
        }
    }
}
