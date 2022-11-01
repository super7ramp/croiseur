package com.gitlab.super7ramp.crosswords.api.dictionary;

import java.util.Objects;

/**
 * The request to list entries for a specific dictionary.
 */
public interface ListDictionaryEntriesRequest {

    /**
     * Creates a new {@link ListDictionariesRequest} from given dictionary identifier.
     *
     * @param dictionaryIdentifier the dictionary identifier
     * @return a new {@link ListDictionariesRequest}
     */
    static ListDictionaryEntriesRequest of(final DictionaryIdentifier dictionaryIdentifier) {
        Objects.requireNonNull(dictionaryIdentifier);
        return () -> dictionaryIdentifier;
    }

    /**
     * Returns the identifier of the desired dictionary.
     *
     * @return the identifier of the desired dictionary
     */
    DictionaryIdentifier dictionaryIdentifier();

}
