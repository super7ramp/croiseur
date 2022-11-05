package com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.common.ParserException;

final class MissingAffixNameException extends ParserException {

    private static final String MESSAGE = "Missing affix name";

    MissingAffixNameException() {
        super(MESSAGE);
    }
}

