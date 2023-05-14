/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.common;

import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryProviderDetails;
import com.gitlab.super7ramp.croiseur.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Selection of dictionaries among several dictionary providers.
 */
public final class DictionarySelection implements UnaryOperator<Collection<DictionaryProvider>> {

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
        FilteredDictionaryProvider(final DictionaryProvider actualArg,
                                   final Predicate<Dictionary> dictionaryFilterArg) {
            actual = actualArg;
            filteredDictionaries = actualArg.get().stream().filter(dictionaryFilterArg).toList();
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
    private DictionarySelection(final Predicate<DictionaryProvider> providerFilterArg,
                                final Predicate<Dictionary> dictionaryFilterArg) {
        providerFilter = providerFilterArg;
        dictionaryFilter = dictionaryFilterArg;
    }

    /**
     * Creates a search by dictionary name.
     *
     * @param desiredDictionaryName the desired dictionary name
     * @return a {@link DictionarySelection} matching any {@link Dictionary} whose name matches the
     * given name
     */
    public static DictionarySelection byName(final String desiredDictionaryName) {
        return new DictionarySelection(satisfied(), dictionary -> dictionary.details().name()
                                                                            .equals(desiredDictionaryName));
    }

    /**
     * Creates a search by optional dictionary name.
     *
     * @param desiredDictionaryName the desired dictionary name, if any
     * @return a {@link DictionarySelection} matching any {@link Dictionary} whose name matches the
     * given name, or any name if given name is {@link Optional#empty()}
     */
    public static DictionarySelection byOptionalName(final Optional<String> desiredDictionaryName) {
        return desiredDictionaryName.map(DictionarySelection::byName)
                                    .orElseGet(DictionarySelection::any);
    }

    /**
     * Creates a search by dictionary locale - fallbacks on dictionary language if not country is
     * information is present in given locale.
     *
     * @param desiredDictionaryLocale the desired dictionary locale
     * @return a {@link DictionarySelection} matching any {@link Dictionary} whose locale matches
     * the given locale
     */
    public static DictionarySelection byLocale(final Locale desiredDictionaryLocale) {
        if (desiredDictionaryLocale.getCountry().isEmpty()) {
            return byLanguage(desiredDictionaryLocale.getLanguage());
        }
        return new DictionarySelection(satisfied(), dictionary -> dictionary.details().locale()
                                                                            .equals(desiredDictionaryLocale));
    }

    /**
     * Creates a search by dictionary language (i.e. only the language part of the locale).
     *
     * @param desiredDictionaryLanguage the desired dictionary language, as returned by
     *                                  {@link Locale#getLanguage()}
     * @return a {@link DictionarySelection} matching any {@link Dictionary} whose locale matches
     * the given language
     */
    public static DictionarySelection byLanguage(final String desiredDictionaryLanguage) {
        return new DictionarySelection(satisfied(),
                dictionary -> dictionary.details()
                                        .locale()
                                        .getLanguage()
                                        .equals(desiredDictionaryLanguage));
    }

    /**
     * Creates a search by system's locale.
     *
     * @return a {@link DictionarySelection} matching any {@link Dictionary} whose locale matches
     * the system locale
     */
    public static DictionarySelection bySystemLocale() {
        return byLocale(Locale.getDefault());
    }

    /**
     * Creates a search by optional dictionary locale.
     *
     * @param desiredDictionaryLocale the desired dictionary locale, if any
     * @return a {@link DictionarySelection} matching any {@link Dictionary} whose locale matches
     * the given locale, or any locale if given locale is {@link Optional#empty()}
     */
    public static DictionarySelection byOptionalLocale(final Optional<Locale> desiredDictionaryLocale) {
        return desiredDictionaryLocale.map(DictionarySelection::byLocale)
                                      .orElseGet(DictionarySelection::any);
    }

    /**
     * Creates a search by optional dictionary provider.
     *
     * @param desiredProviderName the required provider name, if any
     * @return a {@link DictionarySelection} matching any {@link Dictionary} whose provider name
     * matches the given provider name, or any if given provider name is {@link Optional#empty()}
     */
    public static DictionarySelection byOptionalProvider(final Optional<String> desiredProviderName) {
        return desiredProviderName.map(DictionarySelection::byProvider)
                                  .orElseGet(DictionarySelection::any);
    }

    /**
     * Creates a search by dictionary provider.
     *
     * @param desiredProviderName the required provider name
     * @return a {@link DictionarySelection} matching any {@link Dictionary} whose provider name
     * matches the given provider name
     */
    public static DictionarySelection byProvider(final String desiredProviderName) {
        return new DictionarySelection(provider -> provider.details().name()
                                                           .equals(desiredProviderName),
                satisfied());
    }

    /**
     * Creates a search by {@link DictionaryIdentifier}.
     *
     * @param id the identifier
     * @return the corresponding {@link DictionarySelection}
     */
    public static DictionarySelection byId(final DictionaryIdentifier id) {
        return byProvider(id.providerName()).and(byName(id.dictionaryName()));
    }

    /**
     * Returns the default dictionary selection.
     *
     * @return the default dictionary selection.
     */
    public static DictionarySelection byDefault() {
        // TODO default/preferred dictionary management to be improved
        return byProvider("Local XML Provider").and(bySystemLocale());
    }

    /**
     * Returns the default dictionary selection.
     *
     * @return the default dictionary selection.
     */
    public static DictionarySelection none() {
        return new DictionarySelection(Predicate.not(satisfied()), Predicate.not(satisfied()));
    }

    /**
     * A {@link Predicate} that will match with any input.
     *
     * @return a {@link Predicate} that will match with any input
     */
    public static DictionarySelection any() {
        return new DictionarySelection(satisfied(), satisfied());
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
     * Returns the logical "and" between this {@link DictionarySelection} and the given one.
     *
     * @param other the other {@link DictionarySelection} operand
     * @return the logical "and" between this {@link DictionarySelection} and the given one
     */
    public DictionarySelection and(final DictionarySelection other) {
        return new DictionarySelection(providerFilter.and(other.providerFilter),
                dictionaryFilter.and(other.dictionaryFilter));
    }

    /**
     * Returns the logical "or" between this {@link DictionarySelection} and the given one.
     *
     * @param other the other {@link DictionarySelection} operand
     * @return the logical "or" between this {@link DictionarySelection} and the given one
     */
    public DictionarySelection or(final DictionarySelection other) {
        return new DictionarySelection(providerFilter.or(other.providerFilter),
                dictionaryFilter.or(other.dictionaryFilter));
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

