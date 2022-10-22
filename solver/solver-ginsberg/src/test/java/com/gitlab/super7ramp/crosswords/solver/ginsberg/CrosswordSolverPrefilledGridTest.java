package com.gitlab.super7ramp.crosswords.solver.ginsberg;

import com.gitlab.super7ramp.crosswords.spi.solver.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.solver.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;
import org.junit.jupiter.api.Test;

final class CrosswordSolverPrefilledGridTest {

    @Test
    void partiallyFilled() throws InterruptedException {
        final PuzzleDefinition puzzle = PuzzleDefinitionParser.parsePuzzle("""
                |A| | |
                |B| | |
                |C| | |
                """);
        final Dictionary dictionary = new DictionaryMock("AAA", "BBB", "CDE", "ABC", "ABD", "ABE");

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

        Assertions.assertSuccess("""
                |A|A|A|
                |B|B|B|
                |C|D|E|
                """, result);
    }

    @Test
    void filledSlotNotInDictionary() throws InterruptedException {
        final PuzzleDefinition puzzle = PuzzleDefinitionParser.parsePuzzle("""
                |X| | |
                |Y| | |
                |Z| | |
                """);
        final Dictionary dictionary = new DictionaryMock("AAA", "BBB", "CDE", "ABC", "ABD", "ABE");

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

        Assertions.assertImpossible("""
                |X| | |
                |Y| | |
                |Z| | |
                        """, result);
    }

    @Test
    void entirelyFilled() throws InterruptedException {
        final PuzzleDefinition puzzle = PuzzleDefinitionParser.parsePuzzle("""
                |A|A|A|
                |B|B|B|
                |C|D|E|
                """);
        final Dictionary dictionary = new DictionaryMock("AAA", "BBB", "CDE", "ABC", "ABD", "ABE");

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

        Assertions.assertSuccess("""
                |A|A|A|
                |B|B|B|
                |C|D|E|
                """, result);
    }
}
