package com.gitlab.super7ramp.crosswords.gui.controller.dictionary;

import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionariesRequest;

import java.util.Locale;
import java.util.Optional;

/**
 * Implementation of {@link ListDictionariesRequest}.
 */
final class ListDictionaryRequestImpl implements ListDictionariesRequest {

    /**
     * Constructs an instance.
     */
    ListDictionaryRequestImpl() {
        // Nothing to do.
    }

    @Override
    public Optional<Locale> locale() {
        return Optional.empty();
    }

    @Override
    public Optional<String> provider() {
        return Optional.empty();
    }
}
