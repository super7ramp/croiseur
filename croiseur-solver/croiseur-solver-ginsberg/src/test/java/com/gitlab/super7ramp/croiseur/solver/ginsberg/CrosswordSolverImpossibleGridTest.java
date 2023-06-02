/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg;

import com.gitlab.super7ramp.croiseur.common.GridPosition;
import com.gitlab.super7ramp.croiseur.common.PuzzleDefinition;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;

import static com.gitlab.super7ramp.croiseur.common.GridPosition.at;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link GinsbergCrosswordSolver}: Verify behaviour when faced to impossible grids.
 */
final class CrosswordSolverImpossibleGridTest {

    @Test
    void allSlotsImpossible() throws InterruptedException {
        final PuzzleDefinition puzzle = PuzzleDefinitionParser.parsePuzzle(
                """
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
        final PuzzleDefinition puzzle = PuzzleDefinitionParser.parsePuzzle(
                """
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
        final PuzzleDefinition puzzle = PuzzleDefinitionParser.parsePuzzle(
                """
                |X| | |
                |Y| | |
                |Z| | |
                """);
        final Dictionary dictionary = new DictionaryMock("XXX", "XXZ", "YYX");

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        assertEquals(SolverResult.Kind.IMPOSSIBLE, result.kind());
        // First column and last rows are impossible: "XYZ" and "ZZZ" are not in dictionary
        final Set<GridPosition> expectedUnsolvableBoxes =
                Set.of(at(0, 0), at(0, 1), at(0, 2), at(1, 2), at(2, 2));
        assertEquals(expectedUnsolvableBoxes, result.unsolvableBoxes());
    }

    @Test
    void realWorldImpossible() throws URISyntaxException, IOException, InterruptedException {
        final URL dicUrl =
                Objects.requireNonNull(CrosswordSolverImpossibleGridTest.class.getResource("/fr" +
                                                                                           ".dic"));
        final Path dicPath = Path.of(dicUrl.toURI());
        final DictionaryMock dictionary = new DictionaryMock(dicPath);
        final PuzzleDefinition puzzle = PuzzleDefinitionParser.parsePuzzle(
                """
                |O|S|C|I|L|L|A|T|I|O|N|
                |B|O|A|#| |I| | | | | |
                |S|U|#| |#|T| | | | | |
                |E|#| | | |T| | | | | |
                |D| | | | |O| | | |#| |
                |A| | | | |R|#| |#| | |
                |N| | | | |A| |#| | | |
                |T|R|E|M|B|L|E|M|E|N|T|
                """);

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);
        assertEquals(SolverResult.Kind.IMPOSSIBLE, result.kind());

        // First vertical slot puts too many constraints on lower horizontal slots
        final Set<GridPosition> expectedUnsolvableBoxes =
                Set.of(at(0, 0), at(0, 1), at(0, 2), at(0, 3), at(0, 4), at(0, 5), at(0, 6),
                       at(0, 7));
        assertEquals(expectedUnsolvableBoxes, result.unsolvableBoxes());
    }
}
