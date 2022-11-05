package com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.common.ParserException;

final class UnknownAffItemException extends ParserException {

    private static String MESSAGE = "Unknown item: ";

    UnknownAffItemException(final String message) {
        super(MESSAGE + message);
    }
}
