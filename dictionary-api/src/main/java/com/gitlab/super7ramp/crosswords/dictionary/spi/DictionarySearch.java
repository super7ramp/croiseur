package com.gitlab.super7ramp.crosswords.dictionary.spi;

import com.gitlab.super7ramp.crosswords.dictionary.api.Dictionary;

import java.net.URL;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Search dictionaries among several dictionary providers.
 */
public final class DictionarySearch implements UnaryOperator<Collection<DictionaryProvider>> {

    /**
     * A decorator of {@link DictionaryProvider dictionary provider} filtering the dictionaries
     * given a predicate.
     */
    private static class FilteredDictionaryProvider implements DictionaryProvider {

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
            filteredDictionaries = actualArg.get()
                                            .stream()
                                            .filter(dictionaryFilterArg)
                                            .toList();
        }

        @Override
        public String name() {
            return actual.name();
        }

        @Override
        public String description() {
            return actual.description();
        }

        @Override
        public Collection<Dictionary> get(URL... dictionaryPaths) {
            return filteredDictionaries;
        }

        @Override
        public Quality quality() {
            return actual.quality();
        }
    }

    /** Filter on dictionary provider. */
    private final Predicate<DictionaryProvider> providerFilter;

    /** Filter on dictionary. */
    private final Predicate<Dictionary> dictionaryFilter;

    /**
     * Private constructor, allows construction only using factory methods.
     */
    private DictionarySearch(final Predicate<DictionaryProvider> providerFilterArg,
                             final Predicate<Dictionary> dictionaryFilterArg) {
        providerFilter = providerFilterArg;
        dictionaryFilter = dictionaryFilterArg;
    }

    /**
     * Creates a search by dictionary name.
     *
     * @param desiredDictionaryName the desired dictionary name
     * @return a {@link DictionarySearch} matching any {@link Dictionary} whose name matches the
     * given name
     */
    public static DictionarySearch byName(final String desiredDictionaryName) {
        return new DictionarySearch(satisfied(), dictionary -> dictionary.name()
                                                                         .equals(desiredDictionaryName));
    }

    /**
     * Creates a search by optional dictionary name.
     *
     * @param desiredDictionaryName the desired dictionary name, if any
     * @return a {@link DictionarySearch} matching any {@link Dictionary} whose name matches the
     * given name, or any name if given name is {@link Optional#empty()}
     */
    public static DictionarySearch byOptionalName(final Optional<String> desiredDictionaryName) {
        return desiredDictionaryName.map(DictionarySearch::byName)
                                    .orElseGet(DictionarySearch::includeAll);
    }

    /**
     * Creates a search by dictionary locale.
     *
     * @param desiredDictionaryLocale the desired dictionary locale
     * @return a {@link DictionarySearch} matching any {@link Dictionary} whose locale matches
     * the given locale
     */
    public static DictionarySearch byLocale(final Locale desiredDictionaryLocale) {
        return new DictionarySearch(satisfied(), dictionary -> dictionary.locale()
                                                                         .equals(desiredDictionaryLocale));
    }

    /**
     * Creates a search by optional dictionary locale.
     *
     * @param desiredDictionaryLocale the desired dictionary locale, if any
     * @return a {@link DictionarySearch} matching any {@link Dictionary} whose locale matches
     * the given locale, or any locale if given locale is {@link Optional#empty()}
     */
    public static DictionarySearch byOptionalLocale(final Optional<Locale> desiredDictionaryLocale) {
        return desiredDictionaryLocale.map(DictionarySearch::byLocale)
                                      .orElseGet(DictionarySearch::includeAll);
    }

    /**
     * Creates a search by optional dictionary provider.
     *
     * @param desiredProviderName the required provider name, if any
     * @return a {@link DictionarySearch} matching any {@link Dictionary} whose provider name
     * matches the given provider name, or any if given provider name is {@link Optional#empty()}
     */
    public static DictionarySearch byOptionalProvider(final Optional<String> desiredProviderName) {
        return desiredProviderName.map(DictionarySearch::byProvider)
                                  .orElseGet(DictionarySearch::includeAll);
    }

    /**
     * Creates a search by dictionary provider.
     *
     * @param desiredProviderName the required provider name
     * @return a {@link DictionarySearch} matching any {@link Dictionary} whose provider name
     * matches the given provider name
     */
    public static DictionarySearch byProvider(final String desiredProviderName) {
        return new DictionarySearch(provider -> provider.name()
                                                        .equals(desiredProviderName),
                satisfied());
    }

    /**
     * A {@link Predicate} that will match with any input.
     *
     * @return a {@link Predicate} that will match with any input
     */
    public static DictionarySearch includeAll() {
        return new DictionarySearch(satisfied(), satisfied());
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
     * Returns the logical "and" between this {@link DictionarySearch} and the given one.
     *
     * @param other the other {@link DictionarySearch} operand
     * @return the logical "and" between this {@link DictionarySearch} and the given one
     */
    public DictionarySearch and(final DictionarySearch other) {
        return new DictionarySearch(providerFilter.and(other.providerFilter),
                dictionaryFilter.and(other.dictionaryFilter));
    }

    @Override
    public Collection<DictionaryProvider> apply(final Collection<DictionaryProvider> providers) {
        return providers.stream()
                        .filter(providerFilter)
                        .map(provider -> new FilteredDictionaryProvider(provider,
                                dictionaryFilter))
                        .filter(provider -> !provider.get().isEmpty())
                        .collect(Collectors.toUnmodifiableList());
    }
}

