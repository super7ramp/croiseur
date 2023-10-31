/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.common.dictionary;

import java.util.Locale;
import java.util.Objects;

/**
 * Details about a dictionary.
 *
 * @param name        the name of the dictionary
 * @param locale      the locale of the dictionary
 * @param description a (preferably short) description
 */
public record DictionaryDetails(String name, Locale locale, String description) {

    /**
     * Validates fields.
     *
     * @param name        the name of the dictionary
     * @param locale      the locale of the dictionary
     * @param description a (preferably short) description
     */
    public DictionaryDetails {
        Objects.requireNonNull(name, "Missing name");
        Objects.requireNonNull(locale, "Missing locale");
        Objects.requireNonNull(description, "Missing description");
    }

    /**
     * Creates a dictionary description for an unknown dictionary.
     *
     * @return a dictionary description for an unknown dictionary
     */
    public static DictionaryDetails unknown() {
        return new DictionaryDetails("<Unknown dictionary>", Locale.ENGLISH, "<No description>");
    }
}
