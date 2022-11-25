package com.gitlab.super7ramp.crosswords.gui.controller.dictionary;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionaryEntriesRequest;
import com.gitlab.super7ramp.crosswords.gui.view.model.DictionaryViewModel;

/**
 * Implementation of {@link ListDictionaryEntriesRequest}.
 */
final class ListDictionaryEntriesRequestImpl implements ListDictionaryEntriesRequest {

    /** The dictionary identifier. */
    private final DictionaryIdentifier dictionaryId;

    /**
     * Constructs an instance.
     */
    ListDictionaryEntriesRequestImpl(final DictionaryViewModel dictionaryViewModelArg) {
        dictionaryId = new DictionaryIdentifier(dictionaryViewModelArg.provider(), dictionaryViewModelArg.name());
    }

    @Override
    public DictionaryIdentifier dictionaryIdentifier() {
        return dictionaryId;
    }
}
