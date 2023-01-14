/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.gui.view;

import com.gitlab.super7ramp.crosswords.gui.view.model.DictionaryViewModel;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * Custom cell factory for the dictionary {@link ListView}.
 */
final class DictionaryListCellFactory implements Callback<ListView<DictionaryViewModel>,
        ListCell<DictionaryViewModel>> {

    /**
     * Converts a {@link DictionaryViewModel} to a {@link String}.
     */
    private static final class DictionaryListCellStringConverter extends StringConverter<DictionaryViewModel> {

        /**
         * The format used to display {@link DictionaryViewModel}, i.e. {@literal <name>}.
         */
        private static final String FORMAT = "%s";

        /**
         * Constructs an instance.
         */
        DictionaryListCellStringConverter() {
            // Nothing to do.
        }

        @Override
        public String toString(final DictionaryViewModel item) {
            return String.format(FORMAT, item.name());
        }

        @Override
        public DictionaryViewModel fromString(final String string) {
            throw new UnsupportedOperationException("Unused");
        }
    }

    /** The actual callback. */
    private final Callback<ListView<DictionaryViewModel>, ListCell<DictionaryViewModel>> actual;

    /**
     * Constructs an instance.
     */
    DictionaryListCellFactory() {
        actual = CheckBoxListCell.forListView(DictionaryViewModel::selectedProperty,
                new DictionaryListCellStringConverter());
    }

    @Override
    public ListCell<DictionaryViewModel> call(final ListView<DictionaryViewModel> listView) {
        return actual.call(listView);
    }
}
