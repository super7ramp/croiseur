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
     * This takes < 3s to solve at 1 GHz.
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
                |T|A|R|A|#|C|I|A|#|M|A|E|L|
                |A|R|E|C|#|A|R|N|#|A|N|D|Y|
                |T|E|T|E|#|R|A|I|#|R|E|I|N|
                |A|S|T|R|A|L|#|T|A|G|E|T|E|
                |#|#|#|B|I|O|#|A|O|I|#|#|#|
                |A|B|I|E|S|#|#|#|I|S|A|A|C|
                |B|A|I|#|#|#|#|#|#|#|V|I|L|
                |A|L|I|A|S|#|#|#|B|R|E|L|E|
                |#|#|#|B|A|C|#|C|A|O|#|#|#|
                |A|L|T|A|I|R|#|A|L|B|A|N|E|
                |S|E|A|T|#|A|R|T|#|A|M|A|L|
                |S|O|I|E|#|I|I|I|#|G|E|N|E|
                |A|N|N|E|#|G|A|N|#|E|L|A|N|
                """, result);
    }

    /*
     * This takes < 5 to solve at 1 GHz.
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
                |S|A|I|D|#|V|I|S|#|P|E|R|L|
                |A|R|M|E|#|I|E|L|#|E|G|E|E|
                |N|U|E|R|#|I|R|A|#|R|E|I|N|
                |A|M|I|N|C|I|S|S|E|M|E|N|T|
                |#|#|#|I|I|E|#|H|I|E|#|#|#|
                |A|B|C|E|S|#|#|#|B|A|Z|A|R|
                |B|A|A|R|#|#|#|#|#|B|O|N|I|
                |A|L|L|E|R|#|#|#|B|I|E|R|E|
                |#|#|#|S|A|C|#|M|A|L|#|#|#|
                |I|D|E|N|T|I|T|A|R|I|S|M|E|
                |L|O|B|E|#|R|O|T|#|S|A|I|D|
                |Y|U|L|E|#|A|R|T|#|E|R|S|E|
                |A|R|E|S|#|D|I|E|#|R|I|E|N|
                 """, result);
    }

    /*
     * This takes < 10 s to solve at 1 GHz - which is a bit long.
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
                |R|A|I|D|#|P|A|L|E|T|#|B|A|A|L|
                |A|I|S|Y|#|E|N|O|R|A|#|A|L|G|O|
                |I|S|I|S|#|C|A|L|E|B|#|S|O|D|A|
                |S|E|S|B|A|N|I|A|#|O|A|S|I|E|N|
                |#|#|#|A|M|O|S|#|A|U|N|E|#|#|#|
                |B|A|S|S|E|T|#|C|A|R|O|T|E|N|E|
                |A|L|A|I|N|#|S|O|R|E|N|#|B|A|L|
                |T|E|L|E|#|M|A|R|O|T|#|R|O|T|I|
                |O|S|T|#|H|E|L|E|N|#|M|I|L|A|N|
                |N|E|O|M|E|N|I|E|#|C|A|V|A|L|E|
                |#|#|#|E|R|I|N|#|F|O|I|E|#|#|#|
                |A|C|O|R|E|S|#|A|L|G|E|R|O|I|S|
                |C|A|D|I|#|C|A|L|I|N|#|A|C|R|A|
                |E|D|I|T|#|A|C|A|R|E|#|I|R|A|K|
                |R|E|N|E|#|L|E|N|T|E|#|N|E|N|E|
                """, result);
    }
}
