package com.gitlab.super7ramp.crosswords.gui.viewmodel;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

/**
 * The dictionary view model.
 */
public final class DictionaryViewModel {

    /** The available dictionaries. */
    private final ListProperty<String> dictionaries;

    /**
     * Constructs an instance.
     */
    public DictionaryViewModel() {
        dictionaries = new SimpleListProperty<>(this, "dictionaries",
                FXCollections.observableArrayList());
    }

    /**
     * Returns the available dictionaries.
     *
     * @return the available dictionaries
     */
    public ListProperty<String> dictionaries() {
        return dictionaries;
    }

}
