/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg;

import org.junit.jupiter.api.Test;
import re.belv.croiseur.common.puzzle.PuzzleGrid;

final class CrosswordSolverPrefilledGridTest {

    @Test
    void partiallyFilled() throws InterruptedException {
        final PuzzleGrid puzzle = PuzzleGridParser.parse("""
                |A| | |
                |B| | |
                |C| | |
                """);
        final Dictionary dictionary = new DictionaryMock("AAA", "BBB", "CDE", "ABC", "ABD", "ABE");

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        Assertions.assertSuccess("""
                |A|A|A|
                |B|B|B|
                |C|D|E|
                """, result);
    }

    @Test
    void entirelyFilled() throws InterruptedException {
        final PuzzleGrid puzzle = PuzzleGridParser.parse("""
                |A|A|A|
                |B|B|B|
                |C|D|E|
                """);
        final Dictionary dictionary = new DictionaryMock("AAA", "BBB", "CDE", "ABC", "ABD", "ABE");

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        Assertions.assertSuccess("""
                |A|A|A|
                |B|B|B|
                |C|D|E|
                """, result);
    }
}
