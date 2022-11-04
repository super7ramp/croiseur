package com.gitlab.super7ramp.crosswords.gui.controller.dictionary;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionaryEntriesRequest;

/**
 * Implementation of {@link ListDictionaryEntriesRequest}.
 */
final class ListDictionaryEntriesRequestImpl implements ListDictionaryEntriesRequest {

    /**
     * Constructs an instance.
     */
    ListDictionaryEntriesRequestImpl() {
        // Nothing to do.
    }

    @Override
    public DictionaryIdentifier dictionaryIdentifier() {
        // TODO really implement
        return new DictionaryIdentifier("internal", "fr.obj");
    }
}
