/*
 * SPDX-FileCopyrightText: 2025 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg;

import org.junit.jupiter.api.Test;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.common.puzzle.PuzzleGridParser;

/** Tests on grids with shaded boxes. */
final class CrosswordSolverShadedGridTest {

    /**
     * Solver should handle shaded characters.
     *
     * @throws InterruptedException if program is interrupted while solving
     */
    @Test
    void partiallyShadedGrid() throws InterruptedException {
        final PuzzleGrid puzzle = PuzzleGridParser.parse("""
                | | | |
                | | | |
                | | |#|
                """);
        final Dictionary dictionary = new DictionaryMock("AAA", "BBB", "CD", "ABC", "ABD", "AB");

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        Assertions.assertSuccess("""
                |A|B|C|
                |A|B|D|
                |A|B|#|
                """, result);
    }

    /**
     * Program should return without return if no slot is present.
     *
     * @throws InterruptedException if program is interrupted while solving
     */
    @Test
    void entirelyShadedGrid() throws InterruptedException {
        final PuzzleGrid puzzle = PuzzleGridParser.parse("""
                |#|#|#|
                |#|#|#|
                |#|#|#|
                """);
        final Dictionary dictionary = new DictionaryMock("AAA", "BBB", "CD", "ABC", "ABD", "AB");

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        Assertions.assertSuccess("""
                |#|#|#|
                |#|#|#|
                |#|#|#|
                """, result);
    }
}
