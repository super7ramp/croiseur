package com.gitlab.super7ramp.crosswords.gui.viewmodel;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.Collection;

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
    public ReadOnlyListProperty<String> dictionaries() {
        return dictionaries;
    }

    /**
     * Sets the dictionaries.
     * TODO should only be visible from presenter.
     *
     * @param dictionariesArg the dictionaries
     */
    public void setDictionaries(final Collection<String> dictionariesArg) {
        dictionaries.setAll(dictionariesArg);
    }
}
