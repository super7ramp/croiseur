/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link StringTransformers}.
 */
final class StringTransformersTest {

    @Test
    void punctuation() {
        assertEquals("DVDRW", StringTransformers.removePunctuation().apply("DVD+RW"));
    }

    @Test
    void acceptableCrosswordEntry() {
        assertEquals("DVDRW", StringTransformers.toAcceptableCrosswordEntry().apply("dvd+rw"));
    }
}
