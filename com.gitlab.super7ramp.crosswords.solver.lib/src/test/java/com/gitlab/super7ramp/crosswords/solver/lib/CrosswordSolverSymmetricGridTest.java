package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.Dictionary;
import com.gitlab.super7ramp.crosswords.solver.api.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

import static com.gitlab.super7ramp.crosswords.solver.lib.Assertions.assertEquals;
import static com.gitlab.super7ramp.crosswords.solver.lib.PuzzleDefinitionParser.parsePuzzle;

/**
 * Tests on classic Anglo-Saxon symmetric grids.
 */
final class CrosswordSolverSymmetricGridTest {

    /** Test dictionary filename. */
    private static final String DIC_FILE = "/fr.dic";

    private static Dictionary createDictionary() throws IOException, URISyntaxException {
        final Path dicPath = Path.of(CrosswordSolverSymmetricGridTest.class.getResource(DIC_FILE).toURI());
        return new DictionaryMock(dicPath);
    }

    @Test
    void shaded5x5() throws URISyntaxException, IOException, InterruptedException {
        final PuzzleDefinition puzzle = parsePuzzle(
                """
                        |#|#| | | |
                        |#| | | | |
                        | | | | | |
                        | | | | |#|
                        | | | |#|#|
                        """);
        final Dictionary dictionary = createDictionary();

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

        assertEquals(
                """
                        |#|#|A|N|R|
                        |#|A|D|O|S|
                        |A|B|A|C|A|
                        |D|O|M|E|#|
                        |P|I|S|#|#|
                        """, result);
    }

    @Test
    void shaded9x9() throws URISyntaxException, IOException, InterruptedException {
        final PuzzleDefinition puzzle = parsePuzzle(
                """
                        |#|#|#| | | |#|#|#|
                        |#|#| | | | | |#|#|
                        |#| | | | | | | |#|
                        | | | | |#| | | | |
                        | | | |#|#|#| | | |
                        | | | | |#| | | | |
                        |#| | | | | | | |#|
                        |#|#| | | | | |#|#|
                        |#|#|#| | | |#|#|#|
                        """);
        final Dictionary dictionary = createDictionary();

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

        assertEquals(
                """
                        |#|#|#|A|B|S|#|#|#|
                        |#|#|B|R|O|U|T|#|#|
                        |#|A|R|E|T|I|E|R|#|
                        |A|B|U|S|#|F|R|A|C|
                        |D|O|L|#|#|#|R|G|S|
                        |P|R|O|U|#|B|I|O|S|
                        |#|D|I|L|U|A|N|T|#|
                        |#|#|R|I|S|E|E|#|#|
                        |#|#|#|S|A|S|#|#|#|
                        """, result);
    }

    @Test
    @Disabled("Solver not performant enough for now")
    void shaded13x13() throws URISyntaxException, IOException, InterruptedException {
        final PuzzleDefinition puzzle = parsePuzzle(
                """
                        | | | | |#| | | |#| | | | |
                        | | | | |#| | | |#| | | | |
                        | | | | |#| | | |#| | | | |
                        | | | | | | |#| | | | | | |
                        |#|#|#| | | |#| | | |#|#|#|
                        | | | | | |#|#|#| | | | | |
                        | | | |#|#|#|#|#|#|#| | | |
                        | | | | | |#|#|#| | | | | |
                        |#|#|#| | | |#| | | |#|#|#|
                        | | | | | | |#| | | | | | |
                        | | | | |#| | | |#| | | | |
                        | | | | |#| | | |#| | | | |
                        | | | | |#| | | |#| | | | |
                        """);
        final Dictionary dictionary = createDictionary();

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

        assertEquals(
                """
                        | | | | |#| | | |#| | | | |
                        | | | | |#| | | |#| | | | |
                        | | | | |#| | | |#| | | | |
                        | | | | | | |#| | | | | | |
                        |#|#|#| | | |#| | | |#|#|#|
                        | | | | | |#|#|#| | | | | |
                        | | | |#|#|#|#|#|#|#| | | |
                        | | | | | |#|#|#| | | | | |
                        |#|#|#| | | |#| | | |#|#|#|
                        | | | | | | |#| | | | | | |
                        | | | | |#| | | |#| | | | |
                        | | | | |#| | | |#| | | | |
                        | | | | |#| | | |#| | | | |
                        """, result);
    }

    @Test
    @Disabled("Solver not performant enough for now")
    void shaded15x15() throws URISyntaxException, IOException, InterruptedException {
        final PuzzleDefinition puzzle = parsePuzzle(
                """
                        | | | | |#| | | | | |#| | | | |
                        | | | | |#| | | | | |#| | | | |
                        | | | | |#| | | | | |#| | | | |
                        | | | | | | | | |#| | | | | | |
                        |#|#|#| | | | |#| | | | |#|#|#|
                        | | | | | | |#| | | | | | | | |
                        | | | | | |#| | | | | |#| | | |
                        | | | | |#| | | | | |#| | | | |
                        | | | |#| | | | | |#| | | | | |
                        | | | | | | | | |#| | | | | | |
                        |#|#|#| | | | |#| | | | |#|#|#|
                        | | | | | | |#| | | | | | | | |
                        | | | | |#| | | | | |#| | | | |
                        | | | | |#| | | | | |#| | | | |
                        | | | | |#| | | | | |#| | | | |
                        """);
        final Dictionary dictionary = createDictionary();

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

        assertEquals(
                """
                        | | | | |#| | | |#| | | | |
                        | | | | |#| | | |#| | | | |
                        | | | | |#| | | |#| | | | |
                        | | | | | | |#| | | | | | |
                        |#|#|#| | | |#| | | |#|#|#|
                        | | | | | |#|#|#| | | | | |
                        | | | |#|#|#|#|#|#|#| | | |
                        | | | | | |#|#|#| | | | | |
                        |#|#|#| | | |#| | | |#|#|#|
                        | | | | | | |#| | | | | | |
                        | | | | |#| | | |#| | | | |
                        | | | | |#| | | |#| | | | |
                        | | | | |#| | | |#| | | | |
                        """, result);
    }
}
