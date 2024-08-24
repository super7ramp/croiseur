/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.dictionary.selection;

import java.util.Comparator;
import java.util.Locale;
import re.belv.croiseur.common.dictionary.ProvidedDictionaryDetails;

/**
 * Compares dictionaries by preference.
 *
 * <p>The smaller, the more preferred.
 *
 * <p>The criteria used to compare dictionaries are, by order of preference:
 *
 * <ul>
 *   <li>Locale: Dictionary matching system's locale (language + country) is preferred over one which doesn't;
 *   <li>Language: Dictionary matching system's language is preferred over one which doesn't;
 *   <li>Fallback language: Dictionary matching English language is preferred over one which doesn't;
 *   <li>Provider: Dictionary of provider "Local XML Provider" is preferred over one of another type;
 *   <li>Name: Dictionary whose dictionary identifier string representation is smaller (in lexicographical sense) is
 *       preferred. (This is not a relevant criterion, it is used to guarantee the dictionary list can be completely
 *       sorted, assuming that identifiers are unique.)
 * </ul>
 *
 * Concerning locale criterion: The system locale at construction time is used. It means that this comparator shouldn't
 * be cached if changing locale support is a requirement.
 */
public final class DictionaryComparator implements Comparator<ProvidedDictionaryDetails> {

    /** Comparator for dictionary locales. */
    private static final class LocaleComparator implements Comparator<Locale> {

        /** The English language. */
        private static final String ENGLISH_LANGUAGE = Locale.ENGLISH.getLanguage();

        /** The default system locale. */
        private final Locale defaultSystemLocale;

        /** The default system language. */
        private final String defaultSystemLanguage;

        /** Constructs an instance. */
        LocaleComparator() {
            defaultSystemLocale = Locale.getDefault();
            defaultSystemLanguage = defaultSystemLocale.getLanguage();
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
            if (left.getLanguage().equals(defaultSystemLanguage)
                    && !right.getLanguage().equals(defaultSystemLanguage)) {
                return -1;
            }
            if (!left.getLanguage().equals(defaultSystemLanguage)
                    && right.getLanguage().equals(defaultSystemLanguage)) {
                return 1;
            }
            // At this point: Both languages are not default system language.
            if (left.getLanguage().equals(ENGLISH_LANGUAGE)
                    && !right.getLanguage().equals(ENGLISH_LANGUAGE)) {
                return -1;
            }
            if (!left.getLanguage().equals(ENGLISH_LANGUAGE)
                    && right.getLanguage().equals(ENGLISH_LANGUAGE)) {
                return 1;
            }
            // Use a default criterion
            return left.getLanguage().compareTo(right.getLanguage());
        }
    }

    /** Comparator for dictionary providers. */
    private static final class ProviderNameComparator implements Comparator<String> {

        /** The name of the preferred dictionary. */
        // TODO to be passed by configuration
        private static final String PREFERRED_PROVIDER = "Local XML Provider";

        /** Constructs an instance. */
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
            return 0;
        }
    }

    /** The comparator of dictionary locale/language. */
    private final LocaleComparator localeComparator;

    /** The comparator of dictionary provider. */
    private final ProviderNameComparator providerNameComparator;

    /** Constructs an instance. */
    public DictionaryComparator() {
        localeComparator = new LocaleComparator();
        providerNameComparator = new ProviderNameComparator();
    }

    @Override
    public int compare(final ProvidedDictionaryDetails left, final ProvidedDictionaryDetails right) {

        final int localeComparison = localeComparator.compare(left.dictionaryLocale(), right.dictionaryLocale());
        if (localeComparison != 0) {
            return localeComparison;
        }

        final int providerComparison = providerNameComparator.compare(left.providerName(), right.providerName());
        if (providerComparison != 0) {
            return providerComparison;
        }

        return (left.providerName() + left.dictionaryName()).compareTo(right.providerName() + right.dictionaryName());
    }
}
