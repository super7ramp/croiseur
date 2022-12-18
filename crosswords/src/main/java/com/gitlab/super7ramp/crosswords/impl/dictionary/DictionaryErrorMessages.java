package com.gitlab.super7ramp.crosswords.impl.dictionary;

/**
 * Some dictionary error messages.
 */
final class DictionaryErrorMessages {

    /** Error message to publish when no dictionary matching the request is found. */
    static final String NO_DICTIONARY_ERROR_MESSAGE = "No dictionary found";

    /** Error message to publish when an ambiguous request is received. */
    static final String AMBIGUOUS_REQUEST_ERROR_MESSAGE = "Ambiguous request: Found " +
            "matching dictionaries for several providers";

    /**
     * Prevents instantiation.
     */
    private DictionaryErrorMessages() {
        // Nothing to do.
    }
}
