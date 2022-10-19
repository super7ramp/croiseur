package com.gitlab.super7ramp.crosswords.api.dictionary;

/**
 * The request to list entries for a specific dictionary.
 */
public interface ListDictionaryEntriesRequest {

    /**
     * Returns the identifier of the desired dictionary.
     *
     * @return the identifier of the desired dictionary
     */
    DictionaryIdentifier dictionaryIdentifier();

}
