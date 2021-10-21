package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.Dictionary;
import com.gitlab.super7ramp.crosswords.solver.api.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;
import org.junit.jupiter.api.Test;

import static com.gitlab.super7ramp.crosswords.solver.lib.Assertions.assertEquals;
import static com.gitlab.super7ramp.crosswords.solver.lib.PuzzleDefinitionParser.parsePuzzle;

/**
 * Tests to stimulate backtracking.
 */
final class CrosswordSolverBacktrakingTest {

    @Test
    void empty4x4() throws InterruptedException {
        final PuzzleDefinition puzzle = parsePuzzle(
                """
                        | | | | |
                        | | | | |
                        | | | | |
                        | | | | |
                        """);
        final Dictionary dictionary =
                new DictionaryMock("AAAA", "BBBB", "CCCC", "ABCD", "ABCE", "ABCF", "ABCG", "DEFG");

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

        assertEquals(
                """
                        |A|A|A|A|
                        |B|B|B|B|
                        |C|C|C|C|
                        |D|E|F|G|
                        """, result);
    }
}
