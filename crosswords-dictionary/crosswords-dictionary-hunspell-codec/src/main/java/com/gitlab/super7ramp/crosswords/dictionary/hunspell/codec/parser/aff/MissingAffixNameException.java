package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.ParserException;

final class MissingAffixNameException extends ParserException {

    private static final String MESSAGE = "Missing affix name";

    MissingAffixNameException() {
        super(MESSAGE);
    }
}

