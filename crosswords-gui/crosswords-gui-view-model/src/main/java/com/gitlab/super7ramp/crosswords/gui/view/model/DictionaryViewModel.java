/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.gui.view.model;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryKey;
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

    /**
     * Constructs an instance.
     *
     * @param localeArg   the dictionary locale
     * @param providerArg the dictionary provider
     * @param nameArg     the dictionary name
     */
    public DictionaryViewModel(final Locale localeArg, final String providerArg,
                               final String nameArg) {
        selected = new SimpleBooleanProperty(this, "selected", false);
        locale = localeArg;
        provider = providerArg;
        name = nameArg;
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
                '}';
    }
}
