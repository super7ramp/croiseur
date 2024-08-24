/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

import java.util.Locale;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/** A dictionary list view entry. */
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
     * @param providerArg the dictionary provider
     * @param nameArg the dictionary name
     * @param localeArg the dictionary locale
     * @param descriptionArg the dictionary description
     */
    public DictionaryViewModel(
            final String providerArg, final String nameArg, final Locale localeArg, final String descriptionArg) {
        selected = new SimpleBooleanProperty(this, "selected", false);
        provider = providerArg;
        name = nameArg;
        locale = localeArg;
        description = descriptionArg;
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

    /** Sets the value of the selected property to {@code true}. */
    public void select() {
        selected.set(true);
    }

    /** Sets the value of the selected property to {@code false}. */
    public void deselect() {
        selected.set(false);
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
        return "Dictionary{" + "selected="
                + selected.get() + ", locale='"
                + locale + '\'' + ", provider='"
                + provider + '\'' + ", name='"
                + name + '\'' + ", description='"
                + description + '\'' + '}';
    }
}
