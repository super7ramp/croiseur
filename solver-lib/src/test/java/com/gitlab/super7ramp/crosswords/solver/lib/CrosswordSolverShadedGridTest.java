package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.Dictionary;
import com.gitlab.super7ramp.crosswords.solver.api.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;
import org.junit.jupiter.api.Test;

import static com.gitlab.super7ramp.crosswords.solver.lib.Assertions.assertEquals;
import static com.gitlab.super7ramp.crosswords.solver.lib.PuzzleDefinitionParser.parsePuzzle;

/**
 * Tests on grids with shaded boxes.
 */
final class CrosswordSolverShadedGridTest {

    /**
     * Solver should handle shaded characters.
     *
     * @throws InterruptedException if program is interrupted while solving
     */
    @Test
    void partiallyShadedGrid() throws InterruptedException {
        final PuzzleDefinition puzzle = parsePuzzle(
                """
                        | | | |
                        | | | |
                        | | |#|
                        """);
        final Dictionary dictionary = new DictionaryMock("AAA", "BBB", "CD", "ABC", "ABD", "AB");

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

        assertEquals(
                """
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
        final PuzzleDefinition puzzle = parsePuzzle(
                """
                        |#|#|#|
                        |#|#|#|
                        |#|#|#|
                        """);
        final Dictionary dictionary = new DictionaryMock("AAA", "BBB", "CD", "ABC", "ABD", "AB");

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

        assertEquals(
                """
                        |#|#|#|
                        |#|#|#|
                        |#|#|#|
                        """, result);
    }
}
