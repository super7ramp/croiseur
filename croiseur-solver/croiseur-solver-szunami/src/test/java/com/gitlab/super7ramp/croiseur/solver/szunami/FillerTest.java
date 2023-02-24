/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.szunami;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link Filler}.
 */
final class FillerTest {

    /**
     * Verifies that native code panic upon a {@code null} {@link Crossword} is correctly caught
     * and turned into a {@link NativePanicException}.
     */
    @Test
    void failureNullCrossword() {
        final Dictionary emptyDictionary = new Dictionary(Collections.emptyList());

        final NativePanicException exception = assertThrows(NativePanicException.class,
                () -> new Filler().fill(null, emptyDictionary));
        assertEquals("Call to com.gitlab.super7ramp.croiseur.solver.szunami.Crossword method " +
                "failed: NullPtr(\"call_method obj argument\")", exception.getMessage());
    }

    /**
     * Simple 3x3 with a tailored dictionary.
     */
    @Test
    void possible3x3() {
        final Crossword crossword = new Crossword("""
                \s\s\s
                \s\s\s
                \s\s\s
                """, 3, 3);
        final Dictionary dictionary = new Dictionary(List.of("AAA", "BBB", "CDE", "ABC", "ABD",
                "ABE"));

        final Result result = new Filler().fill(crossword, dictionary);

        assertTrue(result.isOk());
        assertEquals("""
                ABC
                ABD
                ABE
                """, result.solution().contents());
    }

    /**
     * Dictionary is empty so there is no solution.
     */
    @Test
    void impossible3x3() {
        final Crossword crossword = new Crossword("""
                \s\s\s
                \s\s\s
                \s\s\s
                """, 3, 3);
        final Dictionary emptyDictionary = new Dictionary(Collections.emptyList());

        final Result result = new Filler().fill(crossword, emptyDictionary);

        assertTrue(result.isErr());
        assertEquals("We failed" /* :) */, result.error());
    }
}
