package com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.dic;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.common.ParserException;

final class InvalidDicEntryException extends ParserException {

    private static final String MESSAGE = "Invalid dic entry: ";

    InvalidDicEntryException(final String invalidEntry) {
        super(MESSAGE + invalidEntry);
    }
}
