/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.solver.ginsberg;

import com.gitlab.super7ramp.crosswords.common.GridPosition;
import com.gitlab.super7ramp.crosswords.common.PuzzleDefinition;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link GinsbergCrosswordSolver}: Verify behaviour when faced to impossible grids.
 */
final class CrosswordSolverImpossibleGridTest {

    @Test
    void allSlotsImpossible() throws InterruptedException {
        final PuzzleDefinition puzzle = PuzzleDefinitionParser.parsePuzzle("""
                | | | |
                | | | |
                | | | |
                """);
        final Dictionary dictionary = new DictionaryMock(/* no word */);

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        assertEquals(SolverResult.Kind.IMPOSSIBLE, result.kind());
        assertEquals(9, result.unsolvableBoxes().size());
    }

    @Test
    void oneSlotImpossible() throws InterruptedException {
        final PuzzleDefinition puzzle = PuzzleDefinitionParser.parsePuzzle("""
                |X| | |
                |Y| | |
                |Z| | |
                """);
        final Dictionary dictionary = new DictionaryMock("XXX", "ZZZ", "XXZ", "YYX");

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        assertEquals(SolverResult.Kind.IMPOSSIBLE, result.kind());
        // First column is impossible: "XYZ" is not in dictionary
        final Set<GridPosition> expectedUnsolvableBoxes = Set.of(new GridPosition(0, 0),
                new GridPosition(0, 1), new GridPosition(0, 2));
        assertEquals(expectedUnsolvableBoxes, result.unsolvableBoxes());
    }

    @Test
    void twoSlotsImpossible() throws InterruptedException {
        final PuzzleDefinition puzzle = PuzzleDefinitionParser.parsePuzzle("""
                |X| | |
                |Y| | |
                |Z| | |
                """);
        final Dictionary dictionary = new DictionaryMock("XXX", "XXZ", "YYX");

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        assertEquals(SolverResult.Kind.IMPOSSIBLE, result.kind());
        // First column and last rows are impossible: "XYZ" and "ZZZ" are not in dictionary
        final Set<GridPosition> expectedUnsolvableBoxes = Set.of(new GridPosition(0, 0),
                new GridPosition(0, 1), new GridPosition(0, 2), new GridPosition(1, 2),
                new GridPosition(2, 2));
        assertEquals(expectedUnsolvableBoxes, result.unsolvableBoxes());
    }
}
