package com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.common.ParserException;

final class InconsistentNumberOfRules extends ParserException {

    private static final String MESSAGE =
            "Number of parsed rules is inconsistent with header " + "information";

    InconsistentNumberOfRules() {
        super(MESSAGE);
    }
}
