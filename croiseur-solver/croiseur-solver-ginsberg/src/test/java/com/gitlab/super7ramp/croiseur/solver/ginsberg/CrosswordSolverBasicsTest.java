/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg;

import com.gitlab.super7ramp.croiseur.common.PuzzleDefinition;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link GinsbergCrosswordSolver}.
 * <p>
 * These tests verify basic search and termination of the solver. Grids are very simple:
 * <ul>
 *     <li>Small sizes</li>
 *     <li>No shaded boxes</li>
 *     <li>No prefilled boxes</li>
 *     <li>Very controlled dictionary</li>
 * </ul>
 */
final class CrosswordSolverBasicsTest {

    @Test
    @Disabled("Assess whether preventing values to be re-used actually is worth it")
    void empty3x3() throws InterruptedException {
        final PuzzleDefinition puzzle = PuzzleDefinitionParser.parsePuzzle("""
                | | | |
                | | | |
                | | | |
                """);
        final Dictionary dictionary = new DictionaryMock("AAA", "BBB", "CDE", "ABC", "ABD", "ABE");

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        Assertions.assertSuccess("""
                |A|B|C|
                |A|B|D|
                |A|B|E|
                """, result);
    }

    @Test
    void empty3x4() throws InterruptedException {
        final PuzzleDefinition puzzle = PuzzleDefinitionParser.parsePuzzle("""
                | | | |
                | | | |
                | | | |
                | | | |
                """);
        final Dictionary dictionary = new DictionaryMock("AAA", "BBB", "CCC", "DEF", "ABCD",
                "ABCE", "ABCF");

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        Assertions.assertSuccess("""
                |A|A|A|
                |B|B|B|
                |C|C|C|
                |D|E|F|
                """, result);
    }
}
