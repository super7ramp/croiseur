package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.Dictionary;
import com.gitlab.super7ramp.crosswords.solver.api.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;
import org.junit.jupiter.api.Test;

import static com.gitlab.super7ramp.crosswords.solver.lib.Assertions.assertEquals;
import static com.gitlab.super7ramp.crosswords.solver.lib.PuzzleDefinitionParser.parsePuzzle;

/**
 * Tests for {@link CrosswordSolverImpl}.
 * <p>
 * These tests verify basics:
 * <ul>
 *     <li>Simple grids with as little backtracking as possible</li>
 *     <li>Handling of shaded boxes</li>
 *     <li>Handling of prefilled boxes</li>
 * </ul>
 */
final class CrosswordSolverBasicsTest {

    @Test
    void empty3x3() throws InterruptedException {
        final PuzzleDefinition puzzle = parsePuzzle(
                """
                        | | | |
                        | | | |
                        | | | |
                        """);
        final Dictionary dictionary = new DictionaryMock("AAA", "BBB", "CDE", "ABC", "ABD", "ABE");

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

        assertEquals(
                """
                        |A|A|A|
                        |B|B|B|
                        |C|D|E|
                        """, result);
    }

    @Test
    void empty3x4() throws InterruptedException {
        final PuzzleDefinition puzzle = parsePuzzle(
                """
                        | | | |
                        | | | |
                        | | | |
                        | | | |
                        """);
        final Dictionary dictionary = new DictionaryMock("AAA", "BBB", "CCC", "DEF", "ABCD", "ABCE", "ABCF");

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

        assertEquals(
                """
                        |A|A|A|
                        |B|B|B|
                        |C|C|C|
                        |D|E|F|
                        """, result);
    }

    @Test
    void prefilled3x3() {
        // TODO Not implemented yet
        // need a different way to store prefilled value so that they can't be removed by backtracking
    }

    @Test
    void shaded3x3() throws InterruptedException {
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
                        |A|A|A|
                        |B|B|B|
                        |C|D|#|
                        """, result);
    }

}
