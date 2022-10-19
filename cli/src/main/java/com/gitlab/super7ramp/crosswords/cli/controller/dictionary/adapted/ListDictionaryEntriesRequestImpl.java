package com.gitlab.super7ramp.crosswords.cli.controller.dictionary.adapted;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionaryEntriesRequest;

/**
 * Implementation of {@link ListDictionaryEntriesRequest}.
 */
public final class ListDictionaryEntriesRequestImpl implements ListDictionaryEntriesRequest {

    /** The dictionary identifier. */
    private final DictionaryIdentifier identifier;

    /**
     * Constructor.
     *
     * @param anIdentifier the dictionary identifier
     */
    public ListDictionaryEntriesRequestImpl(final DictionaryIdentifier anIdentifier) {
        identifier = anIdentifier;
    }

    @Override
    public DictionaryIdentifier dictionaryIdentifier() {
        return identifier;
    }

}
