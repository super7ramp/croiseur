/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.common;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests on {@link DictionaryPath}.
 */
final class DictionaryPathTest {

    @Test
    void split() {
        final DictionaryPath dictionaryPath = DictionaryPath.of("/a/b/c:/d/e/f:/g/h/*");
        final List<String> paths = dictionaryPath.split();
        assertEquals(List.of("/a/b/c", "/d/e/f", "/g/h/*"), paths);
    }

}
