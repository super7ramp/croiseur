package com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.dic;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.common.ParserException;

final class MissingEstimatedNumberOfEntriesException extends ParserException {

    private static final String MESSAGE = "Estimated number of entries is missing";

    MissingEstimatedNumberOfEntriesException() {
        this("");
    }

    MissingEstimatedNumberOfEntriesException(final String message) {
        super(MESSAGE + ": " + message);
    }
}
