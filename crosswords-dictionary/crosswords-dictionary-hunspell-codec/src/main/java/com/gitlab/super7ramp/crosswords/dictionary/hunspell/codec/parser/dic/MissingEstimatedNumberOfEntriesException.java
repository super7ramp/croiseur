package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.dic;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.ParserException;

final class MissingEstimatedNumberOfEntriesException extends ParserException {

    private static final String MESSAGE = "Estimated number of entries is missing";

    MissingEstimatedNumberOfEntriesException() {
        this("");
    }

    MissingEstimatedNumberOfEntriesException(final String message) {
        super(MESSAGE + ": " + message);
    }
}