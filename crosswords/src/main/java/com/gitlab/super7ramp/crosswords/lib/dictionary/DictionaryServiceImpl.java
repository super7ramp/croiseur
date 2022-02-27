package com.gitlab.super7ramp.crosswords.lib.dictionary;

import com.gitlab.super7ramp.crosswords.api.Publisher;
import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionariesRequest;
import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionaryEntriesRequest;
import com.gitlab.super7ramp.crosswords.dictionary.api.Dictionary;
import com.gitlab.super7ramp.crosswords.dictionary.spi.DictionaryLoader;
import com.gitlab.super7ramp.crosswords.dictionary.spi.DictionaryProvider;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Implementation of {@link DictionaryService}.
 */
public final class DictionaryServiceImpl implements DictionaryService {

    /** Error message to publish when no dictionary matching the request is found. */
    private static final String NO_DICTIONARY_ERROR_MESSAGE = "No dictionary found";

    /** Error message to publish when an ambiguous request is received. */
    private static final String AMBIGUOUS_REQUEST_ERROR_MESSAGE = "Ambiguous request: Found " +
            "matching dictionaries for several " + "providers";

    /** The dictionary loader. */
    private final DictionaryLoader dictionaryLoader;

    /** The publisher. */
    private final Publisher publisher;

    /**
     * Constructor.
     *
     * @param aDictionaryLoader a dictionary loader
     * @param aPublisher        a publisher
     */
    public DictionaryServiceImpl(final DictionaryLoader aDictionaryLoader,
                                 final Publisher aPublisher) {
        dictionaryLoader = aDictionaryLoader;
        publisher = aPublisher;
    }

    /**
     * Create a filter on the dictionary provider.
     *
     * @param backend the required provider name
     * @return a filter on dictionary provider
     */
    private static Predicate<DictionaryProvider> filterProvider(final Optional<String> backend) {
        return backend.map(DictionaryLoader.Search::byProvider)
                      .orElseGet(DictionaryLoader.Search::includeAll);
    }

    /**
     * Create a filter on dictionary locale.
     *
     * @param locale the required locale
     * @return a filter on dictionary locale
     */
    private static Predicate<Dictionary> filterLocale(final Optional<Locale> locale) {
        return locale.map(DictionaryLoader.Search::byLocale)
                     .orElseGet(DictionaryLoader.Search::includeAll);
    }

    @Override
    public void listProviders() {
        final Collection<DictionaryProvider> providers = dictionaryLoader.providers();
        if (providers.isEmpty()) {
            publisher.publishError(NO_DICTIONARY_ERROR_MESSAGE);
        } else {
            publisher.publishDictionaryProviders(providers);
        }
    }

    @Override
    public void listDictionaries(final ListDictionariesRequest request) {
        final Map<DictionaryProvider, Collection<Dictionary>> dictionariesByProviders =
                dictionaryLoader.get(filterProvider(request.provider()),
                        filterLocale(request.locale()));

        if (dictionariesByProviders.isEmpty()) {
            publisher.publishError(NO_DICTIONARY_ERROR_MESSAGE);
        } else {
            publisher.publishDictionaries(dictionariesByProviders);
        }
    }

    @Override
    public void listEntries(final ListDictionaryEntriesRequest request) {
        final Map<DictionaryProvider, Collection<Dictionary>> dictionariesByProviders =
                dictionaryLoader.get(filterProvider(request.dictionaryProvider()),
                        DictionaryLoader.Search.byName(request.dictionaryName()));

        if (dictionariesByProviders.isEmpty()) {
            publisher.publishError(NO_DICTIONARY_ERROR_MESSAGE);
        } else if (dictionariesByProviders.size() > 1) {
            publisher.publishError(AMBIGUOUS_REQUEST_ERROR_MESSAGE + " (" + dictionariesByProviders.keySet() + ")");
        } else {
            final Dictionary dictionary = dictionariesByProviders.values()
                                                                 .iterator()
                                                                 .next()
                                                                 .iterator()
                                                                 .next();
            publisher.publishDictionaryEntries(dictionary);
        }
    }
}
