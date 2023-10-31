/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.xml.codec;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * The header of a dictionary.
 *
 * @param locale       the dictionary language
 * @param names        the names of the dictionary in various languages
 * @param descriptions the descriptions of the dictionary in various languages
 */
public record DictionaryHeader(Locale locale, Map<Locale, String> names,
                               Map<Locale, String> descriptions) {

    /**
     * A classic builder.
     */
    static final class Builder {

        /** The name, in various languages. */
        private final Map<Locale, String> names;

        /** The description, in various languages. */
        private final Map<Locale, String> descriptions;

        /** The dictionary language. */
        private Locale locale;

        /**
         * Constructs an instance.
         */
        Builder() {
            locale = null;
            names = new HashMap<>();
            descriptions = new HashMap<>();
        }

        /**
         * Sets the locale, the language of the dictionary.
         *
         * @param localeArg the language of the dictionary
         * @return this builder
         */
        Builder setLocale(final Locale localeArg) {
            if (locale != null) {
                throw new IllegalStateException("Locale already set");
            }
            locale = localeArg;
            return this;
        }

        /**
         * Adds a dictionary name for the given locale.
         *
         * @param locale the language in which is written the name
         * @param name   a dictionary name
         * @return this builder
         */
        Builder addName(final Locale locale, final String name) {
            names.put(locale, name);
            return this;
        }

        /**
         * Adds a dictionary description for the given locale.
         *
         * @param locale      the language in which is written the description
         * @param description a dictionary description
         * @return this builder
         */
        Builder addDescription(final Locale locale, final String description) {
            descriptions.put(locale, description);
            return this;
        }

        /**
         * Builds the dictionary header.
         *
         * @return the dictionary header
         */
        DictionaryHeader build() {
            Objects.requireNonNull(locale, "Locale not parsed, cannot build dictionary header " +
                    "model");
            if (names.isEmpty()) {
                throw new IllegalArgumentException("No name parsed, cannot build dictionary " +
                        "header model");
            }
            if (descriptions.isEmpty()) {
                throw new IllegalArgumentException("No description parsed, cannot build " +
                        "dictionary header model");
            }
            return new DictionaryHeader(locale, names, descriptions);
        }
    }
}
