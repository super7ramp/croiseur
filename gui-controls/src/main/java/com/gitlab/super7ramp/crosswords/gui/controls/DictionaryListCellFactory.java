package com.gitlab.super7ramp.crosswords.gui.controls;

import com.gitlab.super7ramp.crosswords.gui.controls.model.DictionaryListViewEntry;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * Custom cell factory for the dictionary {@link ListView}.
 */
final class DictionaryListCellFactory implements Callback<ListView<DictionaryListViewEntry>,
        ListCell<DictionaryListViewEntry>> {

    /**
     * Converts a {@link DictionaryListViewEntry} to a {@link String}.
     */
    private static class DictionaryListCellStringConverter extends StringConverter<DictionaryListViewEntry> {

        /**
         * The format used to display {@link DictionaryListViewEntry}, i.e. {@literal <locale>
         * — <provider>}.
         */
        private static final String FORMAT = "%s — %s";

        /**
         * Constructs an instance.
         */
        DictionaryListCellStringConverter() {
            // Nothing to do.
        }

        @Override
        public String toString(DictionaryListViewEntry item) {
            return String.format(FORMAT, item.localeProperty().get(), item.providerProperty()
                                                                          .get());
        }

        @Override
        public DictionaryListViewEntry fromString(final String string) {
            throw new UnsupportedOperationException("Unused");
        }
    }

    /** The actual callback. */
    private final Callback<ListView<DictionaryListViewEntry>, ListCell<DictionaryListViewEntry>> actual;

    /**
     * Constructs an instance.
     */
    DictionaryListCellFactory() {
        actual = CheckBoxListCell.forListView(DictionaryListViewEntry::selectedProperty,
                new DictionaryListCellStringConverter());
    }

    @Override
    public ListCell<DictionaryListViewEntry> call(final ListView<DictionaryListViewEntry> listView) {
        return actual.call(listView);
    }
}
