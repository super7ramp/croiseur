/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec;

import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class HunspellDictionaryReaderTest {

    @Test
    void localeFr() {
        final URL dicFile = HunspellDictionaryReaderTest.class.getResource("/fr.dic");

        final Locale actual = new HunspellDictionaryReader(dicFile).description().locale();

        assertEquals(Locale.FRENCH, actual);
    }
}
