package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.Dictionary;
import com.gitlab.super7ramp.crosswords.solver.api.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

import static com.gitlab.super7ramp.crosswords.solver.lib.Assertions.assertSuccess;
import static com.gitlab.super7ramp.crosswords.solver.lib.PuzzleDefinitionParser.parsePuzzle;

/**
 * Tests on classic Anglo-Saxon symmetric grids.
 */
final class CrosswordSolverSymmetricGridTest {

    /**
     * Test dictionary filename.
     */
    private static final String DIC_FILE = "/fr.dic";

    private static Dictionary createDictionary() throws IOException, URISyntaxException {
        final Path dicPath = Path.of(CrosswordSolverSymmetricGridTest.class.getResource(DIC_FILE)
                                                                           .toURI());
        return new DictionaryMock(dicPath);
    }

    /*
     * This takes < 3 s to solve at 1 GHz.
     */
    @Test
    void shaded5x5() throws URISyntaxException, IOException, InterruptedException {
        final PuzzleDefinition puzzle = parsePuzzle("""
                |#|#| | | |
                |#| | | | |
                | | | | | |
                | | | | |#|
                | | | |#|#|
                """);
        final Dictionary dictionary = createDictionary();

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

        assertSuccess("""
                |#|#|C|I|L|
                |#|R|A|L|E|
                |A|G|L|A|E|
                |B|A|I|N|#|
                |C|A|N|#|#|
                """, result);
    }

    /*
     * This takes < 3 s to solve at 1 GHz. No backtrack necessary.
     */
    @Test
    void shaded9x9() throws URISyntaxException, IOException, InterruptedException {
        final PuzzleDefinition puzzle = parsePuzzle("""
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

        assertSuccess("""
                |#|#|#|R|A|M|#|#|#|
                |#|#|C|O|R|O|N|#|#|
                |#|A|U|B|E|N|A|S|#|
                |A|B|L|E|#|A|I|E|A|
                |B|A|I|#|#|#|V|I|I|
                |A|C|E|R|#|M|E|N|E|
                |#|A|R|A|B|I|T|E|#|
                |#|#|E|T|A|L|E|#|#|
                |#|#|#|P|R|E|#|#|#|
                """, result);
    }

    /*
     * This takes < 5s to solve at 1 GHz.
     */
    @Test
    void shaded13x13() throws URISyntaxException, IOException, InterruptedException {
        final PuzzleDefinition puzzle = parsePuzzle("""
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

        assertSuccess("""
                |T|A|R|A|#|C|T|A|#|M|E|T|A|
                |A|R|E|C|#|A|I|N|#|A|N|O|N|
                |T|E|T|E|#|R|O|I|#|R|E|I|N|
                |A|S|T|R|A|L|#|T|A|G|E|T|E|
                |#|#|#|B|I|O|#|A|O|I|#|#|#|
                |A|B|I|E|S|#|#|#|I|S|A|A|C|
                |B|A|I|#|#|#|#|#|#|#|V|I|L|
                |A|L|I|A|S|#|#|#|B|R|E|L|E|
                |#|#|#|B|A|C|#|C|A|O|#|#|#|
                |A|L|T|A|I|R|#|A|L|B|A|N|E|
                |B|O|R|T|#|A|R|T|#|A|M|A|L|
                |E|R|I|E|#|I|I|I|#|G|E|N|E|
                |L|I|N|E|#|G|A|N|#|E|L|A|N|
                """, result);
    }

    /*
     * This takes < 20s to solve at 1 GHz - which is too long.
     */
    @Test
    void shaded13x13WithLongWords() throws URISyntaxException, IOException, InterruptedException {
        final PuzzleDefinition puzzle = parsePuzzle("""
                | | | | |#| | | |#| | | | |
                | | | | |#| | | |#| | | | |
                | | | | |#| | | |#| | | | |
                | | | | | | | | | | | | | |
                |#|#|#| | | |#| | | |#|#|#|
                | | | | | |#|#|#| | | | | |
                | | | | |#|#|#|#|#| | | | |
                | | | | | |#|#|#| | | | | |
                |#|#|#| | | |#| | | |#|#|#|
                | | | | | | | | | | | | | |
                | | | | |#| | | |#| | | | |
                | | | | |#| | | |#| | | | |
                | | | | |#| | | |#| | | | |
                """);
        final Dictionary dictionary = createDictionary();

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

        assertSuccess("""
                |S|A|A|D|#|C|A|A|#|P|A|C|K|
                |O|R|G|E|#|O|V|E|#|E|B|L|E|
                |F|U|I|R|#|R|A|I|#|R|E|I|N|
                |A|M|O|N|C|E|L|L|E|M|E|N|T|
                |#|#|#|I|I|E|#|A|I|E|#|#|#|
                |A|B|C|E|S|#|#|#|B|A|L|A|I|
                |B|A|A|R|#|#|#|#|#|B|O|N|D|
                |A|L|L|E|R|#|#|#|B|I|G|R|E|
                |#|#|#|S|A|C|#|P|A|L|#|#|#|
                |I|D|E|N|T|I|T|A|R|I|S|M|E|
                |P|O|L|E|#|R|O|T|#|S|A|I|D|
                |S|U|E|E|#|A|R|T|#|E|R|S|E|
                |O|R|E|S|#|D|I|E|#|R|I|E|N|
                 """, result);
    }

    /*
     * This takes < 40 s to solve at 1 GHz - which is too long.
     */
    @Test
    void shaded15x15() throws URISyntaxException, IOException, InterruptedException {

        final PuzzleDefinition puzzle = parsePuzzle("""
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

        assertSuccess("""
                |A|M|A|L|#|S|T|A|F|F|#|A|B|B|E|
                |L|I|M|A|#|A|I|E|U|L|#|B|R|O|L|
                |I|L|E|T|#|M|A|D|R|E|#|A|R|N|M|
                |A|E|R|O|G|A|R|E|#|M|A|T|R|I|E|
                |#|#|#|M|O|R|E|#|S|A|I|E|#|#|#|
                |A|L|A|I|R|E|#|C|O|L|L|E|C|T|E|
                |R|A|M|E|E|#|J|A|B|L|E|#|A|I|S|
                |U|P|A|S|#|C|A|I|R|E|#|C|L|A|P|
                |B|O|N|#|B|A|R|R|E|#|N|A|I|R|A|
                |A|N|D|E|R|S|E|N|#|D|O|N|N|E|R|
                |#|#|#|L|I|E|D|#|S|E|A|N|#|#|#|
                |M|A|R|I|E|R|#|P|A|R|M|E|L|A|N|
                |I|R|E|S|#|N|A|B|L|E|#|T|O|N|I|
                |A|N|E|E|#|E|L|I|S|E|#|T|I|A|N|
                |M|I|L|E|#|R|I|T|A|L|#|E|R|R|E|
                """, result);
    }
}
