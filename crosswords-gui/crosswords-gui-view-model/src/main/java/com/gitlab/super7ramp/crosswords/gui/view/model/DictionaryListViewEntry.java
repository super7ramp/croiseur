package com.gitlab.super7ramp.crosswords.gui.view.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Locale;

/**
 * A dictionary list view entry.
 */
public final class DictionaryListViewEntry {

    /** Whether the dictionary is selected. */
    private final BooleanProperty selected;

    /** The dictionary locale. */
    private final StringProperty locale;

    /** The dictionary provider. */
    private final StringProperty provider;

    /** The dictionary name. */
    private final StringProperty name;

    /**
     * Constructs an instance.
     *
     * @param localeArg   the dictionary locale
     * @param providerArg the dictionary provider
     * @param nameArg     the dictionary name
     */
    public DictionaryListViewEntry(final Locale localeArg, final String providerArg,
                                   final String nameArg) {
        selected = new SimpleBooleanProperty(this, "selected", false);
        locale = new SimpleStringProperty(this, "locale", localeArg.getDisplayName());
        provider = new SimpleStringProperty(this, "provider", providerArg);
        name = new SimpleStringProperty(this, "name", nameArg);
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
        return locale.get();
    }
    /**
     * Returns the dictionary provider.
     *
     * @return the dictionary provider
     */
    public String provider() {
        return provider.get();
    }

    /**
     * Returns the dictionary name.
     *
     * @return the dictionary name
     */
    public String name() {
        return name.get();
    }
}
