package com.gitlab.super7ramp.crosswords.api.dictionary;

/**
 * The dictionary service.
 */
public interface DictionaryUsecase {

    /**
     * Lists available dictionary providers.
     */
    void listProviders();

    /**
     * Lists available dictionaries.
     *
     * @param request the request
     */
    void listDictionaries(final ListDictionariesRequest request);

    /**
     * Lists entries for a dictionary.
     *
     * @param request the request
     */
    void listEntries(final ListDictionaryEntriesRequest request);

}
