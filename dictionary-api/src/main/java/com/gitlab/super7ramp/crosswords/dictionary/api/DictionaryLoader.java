package com.gitlab.super7ramp.crosswords.dictionary.api;

import com.gitlab.super7ramp.crosswords.dictionary.spi.DictionaryProvider;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * Load one or several {@link Dictionary} implementation(s).
 */
public final class DictionaryLoader {

    /**
     * Search predicate factory.
     */
    public static final class Search {

        /**
         * Private constructor, static methods only.
         */
        private Search() {
            // Nothing to do
        }

        /**
         * Search by dictionary name.
         *
         * @param desiredDictionaryName the desired dictionary name
         * @return a {@link Predicate} matching any {@link Dictionary} whose name matches the
         * given name
         */
        public static Predicate<Dictionary> byName(final String desiredDictionaryName) {
            return dictionary -> dictionary.name().equals(desiredDictionaryName);
        }

        /**
         * Search by dictionary locale.
         *
         * @param desiredDictionaryLocale the desired dictionary locale
         * @return a {@link Predicate} matching any {@link Dictionary} whose locale matches the
         * given locale
         */
        public static Predicate<Dictionary> byLocale(final Locale desiredDictionaryLocale) {
            return dictionary -> dictionary.locale().equals(desiredDictionaryLocale);
        }

        /**
         * Search by dictionary provider.
         *
         * @param desiredProviderName the desired dictionary provider name
         * @return a {@link Predicate} matching any {@link Dictionary} whose provider name
         * matches the given provider name
         */
        public static Predicate<DictionaryProvider> byProvider(final String desiredProviderName) {
            return provider -> provider.name().equals(desiredProviderName);
        }

        /**
         * A {@link Predicate} that will match with any input.
         *
         * @return a {@link Predicate} that will match with any input
         */
        public static <T> Predicate<T> includeAll() {
            return t -> true;
        }
    }

    /** Service implementation loader. */
    private final ServiceLoader<DictionaryProvider> loader;

    /**
     * Constructor.
     *
     * @param aLoader the actual service loader
     */
    public DictionaryLoader(final ServiceLoader<DictionaryProvider> aLoader) {
        loader = aLoader;
    }

    private static Collection<Dictionary> dictionaries(final DictionaryProvider dictionaryProvider,
                                                       final Predicate<Dictionary> predicate) {
        return dictionaryProvider.get().stream().filter(predicate).toList();
    }

    /**
     * Return the available {@link DictionaryProvider}s.
     *
     * @return the available {@link DictionaryProvider}s
     */
    public Collection<DictionaryProvider> providers() {
        return providers(Search.includeAll()).toList();
    }

    /**
     * Return the dictionaries matching the given predicates, indexed by their provider.
     *
     * @param providerFilter   predicate on the dictionary provider
     * @param dictionaryFilter predicate on the dictionary itself
     * @return the dictionaries matching the given predicates
     */
    public Map<DictionaryProvider, Collection<Dictionary>> get(
            final Predicate<DictionaryProvider> providerFilter,
            final Predicate<Dictionary> dictionaryFilter) {
        return providers(providerFilter).map(provider -> new AbstractMap.SimpleEntry<>(provider,
                                                dictionaries(provider, dictionaryFilter)))
                                        .filter(entry -> !entry.getValue().isEmpty())
                                        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Stream<DictionaryProvider> providers(final Predicate<DictionaryProvider> predicate) {
        return loader.stream().map(ServiceLoader.Provider::get).filter(predicate);
    }

}
