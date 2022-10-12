package com.gitlab.super7ramp.crosswords.api.dictionary;

/**
 * The dictionary service.
 */
public interface DictionaryUsecase {

    /**
     * List available dictionary providers.
     */
    void listProviders();

    /**
     * List available dictionaries.
     *
     * @param request the request
     */
    void listDictionaries(final ListDictionariesRequest request);

    /**
     * List entries for a dictionary.
     *
     * @param request the request
     */
    void listEntries(final ListDictionaryEntriesRequest request);

}
