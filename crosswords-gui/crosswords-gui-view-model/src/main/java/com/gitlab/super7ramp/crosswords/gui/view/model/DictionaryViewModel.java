package com.gitlab.super7ramp.crosswords.gui.view.model;

import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;

/**
 * The dictionary view model.
 */
public final class DictionaryViewModel {

    /** The available dictionaries. */
    private final ListProperty<DictionaryListViewEntry> dictionaries;

    /** The selected dictionaries. */
    private final ReadOnlyListProperty<DictionaryListViewEntry> selectedDictionaries;

    /** The requested dictionary entries. */
    private final ListProperty<String> dictionaryEntries;

    /**
     * Constructs an instance.
     */
    public DictionaryViewModel() {
        dictionaries = new SimpleListProperty<>(this, "dictionaries",
                FXCollections.observableArrayList(entry -> new Observable[]{entry.selectedProperty()}));
        selectedDictionaries = new SimpleListProperty<>(this, "selected dictionaries",
                new FilteredList<>(dictionaries, DictionaryListViewEntry::isSelected));
        dictionaryEntries = new SimpleListProperty<>(this, "dictionary entries",
                FXCollections.observableArrayList());
    }

    /**
     * Returns the available dictionaries.
     *
     * @return the available dictionaries
     */
    public ListProperty<DictionaryListViewEntry> dictionariesProperty() {
        return dictionaries;
    }


    /**
     * Returns the selected dictionaries.
     * <p>
     * Property is read-only, it's just a list filtering the dictionary entries.
     *
     * @return the selected dictionaries
     */
    public ReadOnlyListProperty<DictionaryListViewEntry> selectedDictionariesProperty() {
        return selectedDictionaries;
    }

    /**
     * Returns the requested dictionary entries.
     *
     * @return the requested dictionary entries.
     */
    // TODO change to Map<Dictionary,List<String>>
    public ListProperty<String> dictionaryEntries() {
        return dictionaryEntries;
    }

}
