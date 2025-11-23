/*
 * SPDX-FileCopyrightText: 2025 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static re.belv.croiseur.common.puzzle.GridPosition.at;

import java.util.Set;
import org.junit.jupiter.api.Test;
import re.belv.croiseur.common.puzzle.GridPosition;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.common.puzzle.PuzzleGridParser;

/** Tests for {@link GinsbergCrosswordSolver}: Verify behaviour when faced to impossible grids. */
final class CrosswordSolverImpossibleGridTest {

    @Test
    void allSlotsImpossible() throws InterruptedException {
        final PuzzleGrid puzzle = PuzzleGridParser.parse("""
                | | | |
                | | | |
                | | | |
                """);
        final Dictionary dictionary = new DictionaryMock(/* no word */ );

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        assertEquals(SolverResult.Kind.IMPOSSIBLE, result.kind());
        assertEquals(9, result.unsolvableBoxes().size());
    }

    @Test
    void oneSlotImpossible() throws InterruptedException {
        final PuzzleGrid puzzle = PuzzleGridParser.parse("""
                |X| | |
                |Y| | |
                |Z| | |
                """);
        final Dictionary dictionary = new DictionaryMock("XXX", "ZZZ", "XXZ", "YYX");

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        assertEquals(SolverResult.Kind.IMPOSSIBLE, result.kind());
        // First column is impossible: "XYZ" is not in dictionary
        final Set<GridPosition> expectedUnsolvableBoxes = Set.of(at(0, 0), at(0, 1), at(0, 2));
        assertEquals(expectedUnsolvableBoxes, result.unsolvableBoxes());
    }

    @Test
    void twoSlotsImpossible() throws InterruptedException {
        final PuzzleGrid puzzle = PuzzleGridParser.parse("""
                |X| | |
                |Y| | |
                |Z| | |
                """);
        final Dictionary dictionary = new DictionaryMock("XXX", "XXZ", "YYX");

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        assertEquals(SolverResult.Kind.IMPOSSIBLE, result.kind());
        // First column and last rows are impossible: "XYZ" and "ZZZ" are not in dictionary
        final Set<GridPosition> expectedUnsolvableBoxes = Set.of(at(0, 0), at(0, 1), at(0, 2), at(1, 2), at(2, 2));
        assertEquals(expectedUnsolvableBoxes, result.unsolvableBoxes());
    }
}
