package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.Dictionary;
import com.gitlab.super7ramp.crosswords.solver.api.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

import static com.gitlab.super7ramp.crosswords.solver.lib.Assertions.assertSuccess;
import static com.gitlab.super7ramp.crosswords.solver.lib.PuzzleDefinitionParser.parsePuzzle;

/**
 * Tests on some asymmetric French grids.
 */
public class CrosswordSolverAsymmetricGridTest {

    /** Test dictionary filename. */
    private static final String DIC_FILE = "/fr.dic";

    private static Dictionary createDictionary() throws IOException, URISyntaxException {
        final Path dicPath = Path.of(CrosswordSolverSymmetricGridTest.class.getResource(DIC_FILE).toURI());
        return new DictionaryMock(dicPath);
    }

    @Test
    void shaded6x4() throws URISyntaxException, IOException, InterruptedException {
        final PuzzleDefinition puzzle = parsePuzzle(
                """
                        | | | | |#| |
                        | | |#| | | |
                        | | |#| | | |
                        | | |#| | | |
                        """);
        final Dictionary dictionary = createDictionary();

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

        assertSuccess(
                """
                        |S|A|G|A|#|T|
                        |A|I|#|A|B|A|
                        |A|N|#|B|A|I|
                        |D|E|#|A|I|N|
                        """, result);
    }

    @Test
    @Disabled("Solver not performant enough for now / no solution with dictionary?")
    void shaded12x10() throws URISyntaxException, IOException, InterruptedException {
        final PuzzleDefinition puzzle = parsePuzzle(
                """
                        | | | | | | | | | | | | |
                        | | | | |#| | | | | | | |
                        | | | | | | | | | | |#| |
                        | | | | |#| | | |#| | | |
                        | | | | |#| | | | | | | |
                        | | | | | | | |#| | | | |
                        | | | | | | |#| | | |#| |
                        | | |#| | |#| | | |#| | |
                        | | | |#| | | | |#| | | |
                        | | | | | | | | | | | | |
                        """);
        final Dictionary dictionary = createDictionary();

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

        assertSuccess(
                """
                        | | | | | | | | | | | | |
                        | | | | |#| | | | | | | |
                        | | | | | | | | | | |#| |
                        | | | | |#| | | |#| | | |
                        | | | | |#| | | | | | | |
                        | | | | | | | |#| | | | |
                        | | | | | | |#| | | |#| |
                        | | |#| | |#| | | |#| | |
                        | | | |#| | | | |#| | | |
                        | | | | | | | | | | | | |
                        """, result);
    }
}