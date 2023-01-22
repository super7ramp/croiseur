/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.wordforms;

import java.util.Set;

final class WordFormGeneratorCompoundAffixTest extends WordFormGeneratorTestCase {

    @Override
    String name() {
        return "compoundaffix";
    }

    @Override
    Set<String> expected() {
        return Set.of("foo", "prefoo", "foosuf", "prefoosuf",
                "bar", "prebar", "barsuf", "prebarsuf",
                "foobar", "prefoobar", "foobarsuf", "prefoobarsuf",
                "barfoo", "prebarfoo", "barfoosuf", "prebarfoosuf",
                "foofoo", "prefoofoo", "foofoosuf", "prefoofoosuf",
                "barbar", "prebarbar", "barbarsuf", "prebarbarsuf");
    }
}
