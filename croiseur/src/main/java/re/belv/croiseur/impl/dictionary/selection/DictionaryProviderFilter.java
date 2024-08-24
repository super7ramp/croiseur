/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.dictionary.selection;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import re.belv.croiseur.api.dictionary.DictionaryIdentifier;
import re.belv.croiseur.common.dictionary.DictionaryProviderDetails;
import re.belv.croiseur.spi.dictionary.Dictionary;
import re.belv.croiseur.spi.dictionary.DictionaryProvider;

/**
 * Filters dictionary providers using conditions on both the provider details and its dictionaries.
 */
public final class DictionaryProviderFilter implements UnaryOperator<Collection<DictionaryProvider>> {

    /**
     * A decorator of {@link DictionaryProvider dictionary provider} filtering the dictionaries
     * given a predicate.
     */
    private static final class FilteredDictionaryProvider implements DictionaryProvider {

        /** The actual dictionary provider. */
        private final DictionaryProvider actual;

        /** Filter on dictionary. */
        private final Collection<Dictionary> filteredDictionaries;

        /**
         * Constructor.
         *
         * @param actualArg           the actual dictionary provider
         * @param dictionaryFilterArg the filter on dictionary
         */
        FilteredDictionaryProvider(
                final DictionaryProvider actualArg, final Predicate<Dictionary> dictionaryFilterArg) {
            actual = actualArg;
            filteredDictionaries =
                    actualArg.get().stream().filter(dictionaryFilterArg).toList();
        }

        @Override
        public DictionaryProviderDetails details() {
            return actual.details();
        }

        @Override
        public Collection<Dictionary> get() {
            return filteredDictionaries;
        }
    }

    /** Filter on dictionary provider. */
    private final Predicate<DictionaryProvider> providerFilter;

    /** Filter on dictionary. */
    private final Predicate<Dictionary> dictionaryFilter;

    /**
     * Private constructor, allows construction only using factory methods.
     *
     * @param providerFilterArg   the filter on dictionary provider
     * @param dictionaryFilterArg the filter on dictionary
     */
    private DictionaryProviderFilter(
            final Predicate<DictionaryProvider> providerFilterArg, final Predicate<Dictionary> dictionaryFilterArg) {
        providerFilter = providerFilterArg;
        dictionaryFilter = dictionaryFilterArg;
    }

    /**
     * Creates a search by dictionary name.
     *
     * @param desiredDictionaryName the desired dictionary name
     * @return a {@link DictionaryProviderFilter} matching any {@link Dictionary} whose name matches the
     * given name
     */
    public static DictionaryProviderFilter byName(final String desiredDictionaryName) {
        return new DictionaryProviderFilter(
                satisfied(), dictionary -> dictionary.details().name().equals(desiredDictionaryName));
    }

    /**
     * Creates a search by dictionary locale - fallbacks on dictionary language if not country is
     * information is present in given locale.
     *
     * @param desiredDictionaryLocale the desired dictionary locale
     * @return a {@link DictionaryProviderFilter} matching any {@link Dictionary} whose locale matches
     * the given locale
     */
    public static DictionaryProviderFilter byLocale(final Locale desiredDictionaryLocale) {
        if (desiredDictionaryLocale.getCountry().isEmpty()) {
            return byLanguage(desiredDictionaryLocale.getLanguage());
        }
        return new DictionaryProviderFilter(
                satisfied(), dictionary -> dictionary.details().locale().equals(desiredDictionaryLocale));
    }

    /**
     * Creates a search by dictionary language (i.e. only the language part of the locale).
     *
     * @param desiredDictionaryLanguage the desired dictionary language, as returned by
     *                                  {@link Locale#getLanguage()}
     * @return a {@link DictionaryProviderFilter} matching any {@link Dictionary} whose locale matches
     * the given language
     */
    public static DictionaryProviderFilter byLanguage(final String desiredDictionaryLanguage) {
        return new DictionaryProviderFilter(
                satisfied(),
                dictionary -> dictionary.details().locale().getLanguage().equals(desiredDictionaryLanguage));
    }

    /**
     * Creates a search by optional dictionary locale.
     *
     * @param desiredDictionaryLocale the desired dictionary locale, if any
     * @return a {@link DictionaryProviderFilter} matching any {@link Dictionary} whose locale matches
     * the given locale, or any locale if given locale is {@link Optional#empty()}
     */
    public static DictionaryProviderFilter byOptionalLocale(final Optional<Locale> desiredDictionaryLocale) {
        return desiredDictionaryLocale.map(DictionaryProviderFilter::byLocale).orElseGet(DictionaryProviderFilter::any);
    }

    /**
     * Creates a search by optional dictionary provider.
     *
     * @param desiredProviderName the required provider name, if any
     * @return a {@link DictionaryProviderFilter} matching any {@link Dictionary} whose provider name
     * matches the given provider name, or any if given provider name is {@link Optional#empty()}
     */
    public static DictionaryProviderFilter byOptionalProvider(final Optional<String> desiredProviderName) {
        return desiredProviderName.map(DictionaryProviderFilter::byProvider).orElseGet(DictionaryProviderFilter::any);
    }

    /**
     * Creates a search by dictionary provider.
     *
     * @param desiredProviderName the required provider name
     * @return a {@link DictionaryProviderFilter} matching any {@link Dictionary} whose provider name
     * matches the given provider name
     */
    public static DictionaryProviderFilter byProvider(final String desiredProviderName) {
        return new DictionaryProviderFilter(
                provider -> provider.details().name().equals(desiredProviderName), satisfied());
    }

    /**
     * Creates a search by {@link DictionaryIdentifier}.
     *
     * @param id the identifier
     * @return the corresponding {@link DictionaryProviderFilter}
     */
    public static DictionaryProviderFilter byId(final DictionaryIdentifier id) {
        return byProvider(id.providerName()).and(byName(id.dictionaryName()));
    }

    /**
     * Returns the default dictionary selection.
     *
     * @return the default dictionary selection.
     */
    public static DictionaryProviderFilter none() {
        return new DictionaryProviderFilter(Predicate.not(satisfied()), Predicate.not(satisfied()));
    }

    /**
     * A {@link Predicate} that will match with any input.
     *
     * @return a {@link Predicate} that will match with any input
     */
    public static DictionaryProviderFilter any() {
        return new DictionaryProviderFilter(satisfied(), satisfied());
    }

    /**
     * Returns a predicate which is always satisfied.
     *
     * @param <T> the type on which the predicate applies
     * @return a predicate which is always satisfied
     */
    private static <T> Predicate<T> satisfied() {
        return t -> true;
    }

    /**
     * Returns the logical "and" between this {@link DictionaryProviderFilter} and the given one.
     *
     * @param other the other {@link DictionaryProviderFilter} operand
     * @return the logical "and" between this {@link DictionaryProviderFilter} and the given one
     */
    public DictionaryProviderFilter and(final DictionaryProviderFilter other) {
        return new DictionaryProviderFilter(
                providerFilter.and(other.providerFilter), dictionaryFilter.and(other.dictionaryFilter));
    }

    /**
     * Returns the logical "or" between this {@link DictionaryProviderFilter} and the given one.
     *
     * @param other the other {@link DictionaryProviderFilter} operand
     * @return the logical "or" between this {@link DictionaryProviderFilter} and the given one
     */
    public DictionaryProviderFilter or(final DictionaryProviderFilter other) {
        return new DictionaryProviderFilter(
                providerFilter.or(other.providerFilter), dictionaryFilter.or(other.dictionaryFilter));
    }

    @Override
    public Collection<DictionaryProvider> apply(final Collection<DictionaryProvider> providers) {
        return providers.stream()
                .filter(providerFilter)
                .map(provider -> new FilteredDictionaryProvider(provider, dictionaryFilter))
                .filter(provider -> !provider.get().isEmpty())
                .collect(Collectors.toUnmodifiableList());
    }
}
