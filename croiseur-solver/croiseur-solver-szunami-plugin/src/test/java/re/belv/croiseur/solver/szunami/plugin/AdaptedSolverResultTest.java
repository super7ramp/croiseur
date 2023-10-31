/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.szunami.plugin;

import org.junit.jupiter.api.Test;
import re.belv.croiseur.common.puzzle.GridPosition;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.solver.szunami.Crossword;
import re.belv.croiseur.solver.szunami.Result;
import re.belv.croiseur.spi.solver.SolverResult;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link AdaptedSolverResult}.
 */
final class AdaptedSolverResultTest {

    @Test
    void success() {

        final PuzzleGrid originalPuzzle = new PuzzleGrid.Builder()
                .height(3)
                .width(2)
                .shade(new GridPosition(1, 2))
                .fill(new GridPosition(1, 1), 'A')
                .build();
        final Crossword solvedCrossword = new Crossword("""
                XX
                AA
                B*
                """, 2, 3);
        final Result result = Result.ok(solvedCrossword);

        final SolverResult solverResult = new AdaptedSolverResult(result, originalPuzzle);


        assertEquals(SolverResult.Kind.SUCCESS, solverResult.kind());
        assertEquals(Map.of(new GridPosition(0, 0), 'X', new GridPosition(1, 0), 'X'
                        , new GridPosition(0, 1), 'A', new GridPosition(1, 1), 'A',
                        new GridPosition(0, 2), 'B'),
                solverResult.filledBoxes());
        assertTrue(solverResult.unsolvableBoxes().isEmpty());
    }

    @Test
    void impossible() {

        final PuzzleGrid originalPuzzle = new PuzzleGrid.Builder()
                .height(3)
                .width(2)
                .shade(new GridPosition(1, 2))
                .fill(new GridPosition(1, 1), 'A')
                .build();
        final Result result = Result.err("_");

        final SolverResult solverResult = new AdaptedSolverResult(result, originalPuzzle);

        assertEquals(SolverResult.Kind.IMPOSSIBLE, solverResult.kind());
        assertTrue(solverResult.filledBoxes().isEmpty());
        assertEquals(Set.of(new GridPosition(0, 0), new GridPosition(1, 0), new GridPosition(0, 1)
                , new GridPosition(1, 1), new GridPosition(0, 2)), solverResult.unsolvableBoxes());
    }
}
