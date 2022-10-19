package com.gitlab.super7ramp.crosswords.gui.viewmodel;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

/**
 * The dictionary view model.
 */
public final class DictionaryViewModel {

    /** The available dictionaries. */
    private final ListProperty<String> dictionaries;

    /** The selected dictionary. */
    private final StringProperty selectedDictionary;

    /**
     * Constructs an instance.
     */
    public DictionaryViewModel() {
        dictionaries = new SimpleListProperty<>(this, "dictionaries",
                FXCollections.observableArrayList());
        selectedDictionary = new SimpleStringProperty(this, "selected dictionary", "");
    }

    /**
     * Returns the available dictionaries.
     *
     * @return the available dictionaries
     */
    public ListProperty<String> dictionaries() {
        return dictionaries;
    }

    /**
     * Returns the selected dictionary.
     *
     * @return the selected dictionary
     */
    public StringProperty selectedDictionary() {
        return selectedDictionary;
    }

}
