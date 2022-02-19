package com.gitlab.super7ramp.crosswords.cli.dictionary.adapted;

import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionaryEntriesRequest;
import com.gitlab.super7ramp.crosswords.cli.dictionary.parsed.DictionaryIdentifier;

import java.util.Optional;

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
    public Optional<String> dictionaryProvider() {
        return identifier.providerName();
    }

    @Override
    public String dictionaryName() {
        return identifier.dictionaryName();
    }
}
