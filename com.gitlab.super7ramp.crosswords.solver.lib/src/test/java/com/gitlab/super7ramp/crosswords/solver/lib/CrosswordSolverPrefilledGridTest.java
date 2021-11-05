package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.Dictionary;
import com.gitlab.super7ramp.crosswords.solver.api.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.gitlab.super7ramp.crosswords.solver.lib.Assertions.assertEquals;
import static com.gitlab.super7ramp.crosswords.solver.lib.PuzzleDefinitionParser.parsePuzzle;

final class CrosswordSolverPrefilledGridTest {

    @Test
    void partiallyFilled() throws InterruptedException {
        final PuzzleDefinition puzzle = parsePuzzle(
                """
                        |A| | |
                        |B| | |
                        |C| | |
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
    @Disabled("Solver shouldn't return an IllegalStateException when no bactracking solution found")
    void filledSlotNotInDictionary() throws InterruptedException {
        final PuzzleDefinition puzzle = parsePuzzle(
                """
                        |X| | |
                        |Y| | |
                        |Z| | |
                        """);
        final Dictionary dictionary = new DictionaryMock("AAA", "BBB", "CDE", "ABC", "ABD", "ABE");

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

        assertEquals(
                """
                        |X| | |
                        |Y| | |
                        |Z| | |
                                """, result);
    }

    @Test
    void entirelyFilled() throws InterruptedException {
        final PuzzleDefinition puzzle = parsePuzzle(
                """
                        |A|A|A|
                        |B|B|B|
                        |C|D|E|
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
}
