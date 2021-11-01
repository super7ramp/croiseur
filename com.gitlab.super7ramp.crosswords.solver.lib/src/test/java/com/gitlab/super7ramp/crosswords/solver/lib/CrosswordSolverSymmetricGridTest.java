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
                        |#|#|A|M|O|U|R|#|#|
                        |#|A|S|I|N|I|E|N|#|
                        |A|B|E|R|#|F|A|I|M|
                        |D|I|X|#|#|#|L|O|I|
                        |P|E|U|R|#|J|I|L|L|
                        |#|S|E|A|G|A|T|E|#|
                        |#|#|L|I|A|N|E|#|#|
                        |#|#|#|L|I|E|#|#|#|
                        """, result);
    }

    @Test
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
                        |D|E|S|S|#|B|A|L|#|A|B|O|T|
                        |E|X|I|T|#|L|I|E|#|L|A|B|O|
                        |M|O|R|E|#|U|E|M|#|L|E|U|R|
                        |E|N|E|R|V|E|#|A|G|A|S|S|E|
                        |#|#|#|N|E|T|#|N|D|T|#|#|#|
                        |K|H|M|E|R|#|#|#|F|E|R|M|I|
                        |W|A|I|#|#|#|#|#|#|#|E|I|O|
                        |C|L|E|B|S|#|#|#|A|A|R|O|N|
                        |#|#|#|L|A|D|#|P|D|G|#|#|#|
                        |E|S|P|E|C|E|#|U|N|E|S|C|O|
                        |B|E|A|U|#|C|A|F|#|N|A|O|S|
                        |L|A|R|E|#|O|D|F|#|D|A|T|E|
                        |E|T|A|T|#|R|O|Y|#|A|B|E|R|
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
                        """, result);
    }
}
