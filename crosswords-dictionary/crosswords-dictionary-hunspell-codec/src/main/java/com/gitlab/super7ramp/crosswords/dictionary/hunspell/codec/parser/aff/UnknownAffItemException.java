package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.ParserException;

final class UnknownAffItemException extends ParserException {

    private static final String MESSAGE = "Unknown item: ";

    UnknownAffItemException(final String message) {
        super(MESSAGE + message);
    }
}
