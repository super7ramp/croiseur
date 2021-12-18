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
     * This takes < 5 s to solve at 1 GHz.
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
     * This takes < 10s to solve at 1 GHz.
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
                |S|O|R|E|#|B|O|A|#|P|A|R|A|
                |A|R|E|S|#|E|S|S|#|A|N|O|N|
                |R|E|I|S|#|R|E|M|#|S|A|M|E|
                |D|E|N|A|I|N|#|A|S|T|R|E|E|
                |#|#|#|R|I|E|#|A|S|I|#|#|#|
                |E|P|I|T|E|#|#|#|I|S|A|A|C|
                |R|A|I|#|#|#|#|#|#|#|V|I|L|
                |E|L|I|A|S|#|#|#|B|R|E|L|E|
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
                |S|A|L|E|#|P|P|P|#|M|O|X|A|
                |A|L|I|X|#|E|R|E|#|O|L|L|N|
                |A|D|N|C|#|N|E|T|#|N|E|V|E|
                |D|I|A|L|E|C|T|O|L|O|G|I|E|
                |#|#|#|U|R|E|#|N|E|M|#|#|#|
                |B|A|I|S|E|#|#|#|V|O|C|A|L|
                |I|M|S|I|#|#|#|#|#|R|O|S|E|
                |P|I|A|V|E|#|#|#|S|P|L|I|T|
                |#|#|#|E|S|A|#|B|A|H|#|#|#|
                |P|Y|G|M|A|L|I|O|N|I|S|M|E|
                |H|A|L|E|#|L|O|L|#|S|A|A|D|
                |I|L|A|N|#|O|D|E|#|M|A|Y|A|
                |L|E|S|T|#|C|E|E|#|E|D|A|M|
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
                |R|A|J|A|#|A|L|T|G|R|#|C|N|R|S|
                |A|R|E|C|#|D|A|R|I|O|#|R|O|U|E|
                |T|U|E|R|#|R|I|A|N|T|#|O|I|N|T|
                |E|M|P|O|R|I|U|M|#|A|S|T|R|E|E|
                |#|#|#|B|A|E|S|#|P|R|E|T|#|#|#|
                |C|O|P|A|I|N|#|R|A|I|N|E|T|E|R|
                |A|R|I|T|E|#|P|A|T|E|E|#|I|V|E|
                |R|I|P|E|#|R|A|M|O|N|#|B|A|I|N|
                |R|O|I|#|M|A|T|O|N|#|M|A|R|E|E|
                |E|N|T|R|A|V|O|N|#|G|A|L|E|R|E|
                |#|#|#|O|R|E|N|#|D|A|N|A|#|#|#|
                |A|I|R|A|I|N|#|A|I|M|A|N|T|I|N|
                |R|E|I|N|#|A|L|L|E|E|#|I|I|I|E|
                |N|E|O|N|#|L|I|A|N|T|#|T|A|I|N|
                |M|E|M|E|#|A|N|N|E|E|#|E|N|E|E|
                """, result);
    }
}
