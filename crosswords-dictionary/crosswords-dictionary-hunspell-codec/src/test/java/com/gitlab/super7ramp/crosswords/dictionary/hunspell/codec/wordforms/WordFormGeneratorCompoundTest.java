/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.wordforms;

import java.util.Set;

final class WordFormGeneratorCompoundTest extends WordFormGeneratorTestCase {

    @Override
    String name() {
        return "compound";
    }

    @Override
    Set<String> expected() {
        return Set.of("foo", "bar", "foobar", "barfoo", "foofoo", "barbar");
    }
}
