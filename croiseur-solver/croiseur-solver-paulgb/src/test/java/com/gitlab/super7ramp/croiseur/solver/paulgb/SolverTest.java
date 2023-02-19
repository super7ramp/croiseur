/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.paulgb;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link Solver}.
 */
final class SolverTest {

    /**
     * Grid is void, so result is present but the encapsulated array is empty.
     *
     * @throws SolverErrorException should not happen
     */
    @Test
    void possible0x0() throws SolverErrorException {
        final Puzzle puzzle = new Puzzle(new int[0][0]);
        final Dictionary emptyDictionary = new Dictionary(new String[0]);

        final Optional<Solution> solution = new Solver().solve(puzzle, emptyDictionary);

        assertTrue(solution.isPresent());
        assertEquals(0, solution.get().cells().length);
    }

    /**
     * Dictionary is empty so there is no solution.
     *
     * @throws SolverErrorException should not happen
     */
    @Test
    void impossible3x3() throws SolverErrorException {
        final Puzzle puzzle = new Puzzle(new int[][]{
                // horizontal slots
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8},
                // vertical slots
                {0, 3, 6},
                {1, 4, 7},
                {2, 5, 8}
        });
        final Dictionary emptyDictionary = new Dictionary(new String[0]);

        final Optional<Solution> solution = new Solver().solve(puzzle, emptyDictionary);

        assertTrue(solution.isEmpty());
    }

    /**
     * Simple 3x3 with a tailored dictionary.
     *
     * @throws SolverErrorException should not happen
     */
    @Test
    void possible3x3() throws SolverErrorException {
        final Puzzle puzzle = new Puzzle(new int[][]{
                // horizontal slots
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8},
                // vertical slots
                {0, 3, 6},
                {1, 4, 7},
                {2, 5, 8}
        });
        final Dictionary dictionary = new Dictionary(new String[]{"AAA", "BBB", "CDE", "ABC"
                , "ABD", "ABE"});

        final Optional<Solution> solution = new Solver().solve(puzzle, dictionary);

        assertTrue(solution.isPresent());
        final char[] cells = solution.get().cells();
        assertEquals(9, cells.length);
        assertArrayEquals(new char[]{'A', 'B', 'C', 'A', 'B', 'D', 'A', 'B', 'E'}, cells);
    }

    /**
     * Verifies that Rust panic upon {@code null} puzzle is turned into
     * {@link SolverErrorException}.
     */
    @Test
    @Disabled("Temporarily disabled, jni code to be reworked to be unwind safe")
    void failureNullPuzzle() {
        final SolverErrorException solverError = assertThrows(SolverErrorException.class,
                () -> new Solver().solve(null, new Dictionary(new String[0])));
        assertEquals("Failed to access puzzle slots: NullPtr(\"call_method obj argument\")",
                solverError.getMessage());
    }

    /**
     * Verifies that Rust panic upon {@code null} dictionary is turned into
     * {@link SolverErrorException}.
     */
    @Test
    @Disabled("Temporarily disabled, jni code to be reworked to be unwind safe")
    void failureNullDictionary() {
        final SolverErrorException solverError = assertThrows(SolverErrorException.class,
                () -> new Solver().solve(new Puzzle(new int[0][]), null));
        assertEquals("Failed to access dictionary words: NullPtr(\"call_method obj argument\")",
                solverError.getMessage());
    }
}
