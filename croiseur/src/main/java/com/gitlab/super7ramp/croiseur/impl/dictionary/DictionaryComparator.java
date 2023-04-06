/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.dictionary;

import com.gitlab.super7ramp.croiseur.common.dictionary.ProvidedDictionaryDescription;

import java.util.Comparator;
import java.util.Locale;

/**
 * Compares dictionaries by preference.
 * <p>
 * The smaller, the more preferred.
 * <p>
 * The criteria used to compare dictionaries are, by order of preference:
 * <ul>
 *     <li>Locale: Dictionary matching system's locale (language + country) is preferred over
 *     one which doesn't;</li>
 *     <li>Language: Dictionary matching system's language is preferred over one which doesn't;
 *     </li>
 *     <li>Type: Dictionary of type "internal" is preferred over one of another type;</li>
 *     <li>Name: Dictionary whose dictionary identifier string representation is smaller (in
 *     lexicographical sense) is preferred. (This is not a relevant criterion, it is used to
 *     guarantee the dictionary list can be completely sorted, assuming that identifiers are
 *     unique.)</li>
 * </ul>
 */
final class DictionaryComparator implements Comparator<ProvidedDictionaryDescription> {

    /**
     * Comparator for dictionary locales.
     */
    private static final class LocaleComparator implements Comparator<Locale> {

        /** The default system locale at instance construction time. */
        private final Locale defaultSystemLocale;

        /** The default system language at instance construction time. */
        private final Locale defaultSystemLocaleLanguageOnly;

        /**
         * Constructs an instance.
         */
        LocaleComparator() {
            defaultSystemLocale = Locale.getDefault();
            // Try to match at least on language if not on language + country
            defaultSystemLocaleLanguageOnly =
                    Locale.forLanguageTag(defaultSystemLocale.getLanguage());
        }

        @Override
        public int compare(final Locale left, final Locale right) {
            if (left.equals(defaultSystemLocale) && !right.equals(defaultSystemLocale)) {
                return -1;
            }
            if (!left.equals(defaultSystemLocale) && right.equals(defaultSystemLocale)) {
                return 1;
            }
            // At this point: Both locales are not default system locale.
            if (left.equals(defaultSystemLocaleLanguageOnly) && !right.equals(
                    defaultSystemLocaleLanguageOnly)) {
                return -1;
            }
            if (!left.equals(defaultSystemLocaleLanguageOnly) && right.equals(
                    defaultSystemLocaleLanguageOnly)) {
                return 1;
            }
            // At this point: Both locales are not default system languages.
            // Use a default criterion.
            return left.toLanguageTag().compareTo(right.toLanguageTag());
        }
    }

    /**
     * Comparator for dictionary providers.
     */
    private static final class ProviderNameComparator implements Comparator<String> {

        /** The name of the preferred dictionary. */
        // TODO to be passed by configuration
        private static final String PREFERRED_PROVIDER = "xml";

        /**
         * Constructs an instance.
         */
        ProviderNameComparator() {
            // Nothing to do.
        }

        @Override
        public int compare(final String leftName, final String rightName) {
            if (leftName.equals(PREFERRED_PROVIDER) && !rightName.equals(PREFERRED_PROVIDER)) {
                return -1;
            }
            if (!leftName.equals(PREFERRED_PROVIDER) && rightName.equals(PREFERRED_PROVIDER)) {
                return 1;
            }
            // At this point: None of the providers are the preferred one.
            return leftName.compareTo(rightName);
        }
    }

    /** The comparator of dictionary locale/language. */
    private final LocaleComparator localeComparator;

    /** The comparator of dictionary provider. */
    private final ProviderNameComparator providerNameComparator;

    /**
     * Constructs an instance.
     */
    DictionaryComparator() {
        localeComparator = new LocaleComparator();
        providerNameComparator = new ProviderNameComparator();
    }

    @Override
    public int compare(final ProvidedDictionaryDescription left,
                       final ProvidedDictionaryDescription right) {

        final int localeComparison =
                localeComparator.compare(left.dictionary().locale(), right.dictionary().locale());
        if (localeComparison != 0) {
            return localeComparison;
        }

        final int providerComparison =
                providerNameComparator.compare(left.providerName(), right.providerName());
        if (providerComparison != 0) {
            return providerComparison;
        }

        return left.dictionary().name().compareTo(right.dictionary().name());
    }
}
