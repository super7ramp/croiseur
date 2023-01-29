/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.wordforms;

import org.junit.jupiter.api.Disabled;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Disabled("compounding from continuation not implemented")
final class WordFormGeneratorGermanCompoundingTest extends WordFormGeneratorTestCase {

    @Override
    String name() {
        return "germancompounding";
    }

    @Override
    Charset charset() {
        return StandardCharsets.ISO_8859_1;
    }
}
