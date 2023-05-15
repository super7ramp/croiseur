/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryKey;
import com.gitlab.super7ramp.croiseur.common.dictionary.ProvidedDictionaryDetails;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.Locale;

/**
 * A dictionary list view entry.
 */
public final class DictionaryViewModel {

    /** Whether the dictionary is selected. */
    private final BooleanProperty selected;

    /** The dictionary locale. */
    private final Locale locale;

    /** The dictionary provider. */
    private final String provider;

    /** The dictionary name. */
    private final String name;

    /** The dictionary description. */
    private final String description;

    /**
     * Constructs an instance.
     *
     * @param providedDictionaryDetails details about the dictionary and its provider
     */
    public DictionaryViewModel(final ProvidedDictionaryDetails providedDictionaryDetails) {
        selected = new SimpleBooleanProperty(this, "selected", false);
        provider = providedDictionaryDetails.providerName();
        locale = providedDictionaryDetails.dictionaryLocale();
        name = providedDictionaryDetails.dictionaryName();
        description = providedDictionaryDetails.dictionaryDescription();
    }

    /**
     * Returns the selected property.
     *
     * @return the selected property
     */
    public BooleanProperty selectedProperty() {
        return selected;
    }

    /**
     * Returns whether the dictionary is selected.
     *
     * @return whether the dictionary is selected.
     */
    public boolean isSelected() {
        return selected.get();
    }

    /**
     * Sets the value of the selected property.
     *
     * @param selectedValue the value to set
     */
    public void setSelected(final boolean selectedValue) {
        selected.set(selectedValue);
    }

    /**
     * Returns the dictionary locale.
     *
     * @return the dictionary locale
     */
    public String locale() {
        return locale.getDisplayName();
    }

    /**
     * Returns the dictionary provider.
     *
     * @return the dictionary provider
     */
    public String provider() {
        return provider;
    }

    /**
     * Returns the dictionary name.
     *
     * @return the dictionary name
     */
    public String name() {
        return name;
    }

    /**
     * Returns the dictionary description.
     *
     * @return the dictionary description
     */
    public String description() {
        return description;
    }

    /**
     * Builds a {@link DictionaryKey} from the information of this {@link DictionaryViewModel}.
     *
     * @return the built {@link DictionaryKey}
     */
    public DictionaryKey key() {
        return new DictionaryKey(provider, name, locale);
    }

    @Override
    public String toString() {
        return "Dictionary{" +
               "selected=" + selected.get() +
               ", locale='" + locale + '\'' +
               ", provider='" + provider + '\'' +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               '}';
    }

}
