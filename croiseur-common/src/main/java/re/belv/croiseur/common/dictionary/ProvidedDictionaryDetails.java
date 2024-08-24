/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.common.dictionary;

import java.util.Locale;

/**
 * Gathers provider and dictionary information.
 *
 * @param providerName provider name
 * @param dictionary dictionary description
 */
public record ProvidedDictionaryDetails(String providerName, DictionaryDetails dictionary) {

    /**
     * Returns the dictionary name.
     *
     * <p>Shortcut for {@code dictionary().name()}.
     *
     * @return the dictionary name
     */
    public String dictionaryName() {
        return dictionary.name();
    }

    /**
     * Returns the dictionary locale.
     *
     * <p>Shortcut for {@code dictionary().locale()}.
     *
     * @return the dictionary locale
     */
    public Locale dictionaryLocale() {
        return dictionary.locale();
    }

    /**
     * Returns the dictionary description.
     *
     * <p>Shortcut for {@code dictionary().description()}.
     *
     * @return the dictionary description
     */
    public String dictionaryDescription() {
        return dictionary.description();
    }
}
