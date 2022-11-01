package com.gitlab.super7ramp.crosswords.solver.ginsberg;

import com.gitlab.super7ramp.crosswords.common.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.spi.solver.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;
import org.junit.jupiter.api.Test;

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
        final PuzzleDefinition puzzle = PuzzleDefinitionParser.parsePuzzle("""
                | | | |
                | | | |
                | | |#|
                """);
        final Dictionary dictionary = new DictionaryMock("AAA", "BBB", "CD", "ABC", "ABD", "AB");

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

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
        final PuzzleDefinition puzzle = PuzzleDefinitionParser.parsePuzzle("""
                |#|#|#|
                |#|#|#|
                |#|#|#|
                """);
        final Dictionary dictionary = new DictionaryMock("AAA", "BBB", "CD", "ABC", "ABD", "AB");

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

        Assertions.assertSuccess("""
                |#|#|#|
                |#|#|#|
                |#|#|#|
                """, result);
    }
}
