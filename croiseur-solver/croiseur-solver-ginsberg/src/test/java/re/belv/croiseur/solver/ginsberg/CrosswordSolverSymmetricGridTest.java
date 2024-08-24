/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg;

import static re.belv.croiseur.solver.ginsberg.PuzzleGridParser.parse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import re.belv.croiseur.common.puzzle.PuzzleGrid;

/**
 * Tests on classic Anglo-Saxon symmetric grids.
 */
final class CrosswordSolverSymmetricGridTest {

    /** The test dictionary. */
    private static Dictionary dictionary;

    @BeforeAll
    static void beforeAll() throws IOException, URISyntaxException {
        final URL dicUrl = Objects.requireNonNull(
                CrosswordSolverSymmetricGridTest.class.getResource("/UKACD18plus.txt"),
                "Test dictionary not found, verify the test resources.");
        final Path dicPath = Path.of(dicUrl.toURI());
        dictionary = new DictionaryMock(dicPath);
    }

    @AfterAll
    static void afterAll() {
        dictionary = null;
    }

    /*
     * This takes < 1 s to solve at 1 GHz.
     */
    @Test
    void shaded5x5() throws InterruptedException {
        final PuzzleGrid puzzle = parse(
                """
                                        |#|#| | | |
                                        |#| | | | |
                                        | | | | | |
                                        | | | | |#|
                                        | | | |#|#|
                                        """);

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        Assertions.assertSuccess(
                """
                                 |#|#|H|A|T|
                                 |#|A|I|R|E|
                                 |A|L|L|I|N|
                                 |B|O|D|S|#|
                                 |A|D|A|#|#|
                                 """,
                result);
    }

    /*
     * This takes < 1 s to solve at 1 GHz. No backtrack necessary.
     */
    @Test
    void shaded9x9() throws InterruptedException {
        final PuzzleGrid puzzle = parse(
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

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        Assertions.assertSuccess(
                """
                                 |#|#|#|C|A|T|#|#|#|
                                 |#|#|C|H|I|R|M|#|#|
                                 |#|A|R|A|L|I|A|S|#|
                                 |A|B|A|S|#|M|I|N|A|
                                 |B|A|T|#|#|#|L|E|Y|
                                 |B|Y|E|S|#|S|M|E|E|
                                 |#|A|R|A|L|I|A|S|#|
                                 |#|#|S|A|Y|O|N|#|#|
                                 |#|#|#|R|E|N|#|#|#|
                                 """,
                result);
    }

    /*
     * This takes < 2s to solve at 1 GHz.
     */
    @Test
    void shaded13x13WithLongWords() throws InterruptedException {
        final PuzzleGrid puzzle = parse(
                """
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

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        Assertions.assertSuccess(
                """
                                 |A|T|A|P|#|A|Y|S|#|A|B|B|A|
                                 |R|O|N|A|#|B|A|A|#|G|R|A|N|
                                 |A|L|A|R|#|A|R|Y|#|R|A|F|T|
                                 |B|A|L|A|N|C|E|S|H|E|E|T|S|
                                 |#|#|#|B|A|A|#|T|A|E|#|#|#|
                                 |A|B|B|O|T|#|#|#|S|A|B|R|A|
                                 |B|A|A|L|#|#|#|#|#|B|O|O|T|
                                 |C|A|R|A|T|#|#|#|A|L|A|T|E|
                                 |#|#|#|N|A|S|#|A|R|E|#|#|#|
                                 |E|N|D|U|R|A|B|L|E|N|E|S|S|
                                 |A|I|R|S|#|B|O|A|#|E|A|L|E|
                                 |S|L|A|E|#|R|O|T|#|S|L|A|Y|
                                 |T|E|W|S|#|A|T|E|#|S|E|E|S|
                                  """,
                result);
    }

    /*
     * This takes < 1s to solve at 1 GHz.
     */
    @Test
    void shaded13x13() throws InterruptedException {
        final PuzzleGrid puzzle = parse(
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

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        Assertions.assertSuccess(
                """
                                 |R|A|C|A|#|F|R|A|#|A|M|L|A|
                                 |A|R|U|M|#|A|I|R|#|D|O|E|S|
                                 |M|U|R|E|#|T|O|E|#|D|A|N|A|
                                 |A|M|E|B|A|E|#|C|A|E|S|A|R|
                                 |#|#|#|A|I|S|#|A|I|R|#|#|#|
                                 |A|B|B|E|S|#|#|#|A|S|A|N|A|
                                 |B|A|A|#|#|#|#|#|#|#|B|A|G|
                                 |C|A|R|B|S|#|#|#|D|S|O|M|O|
                                 |#|#|#|E|A|S|#|B|A|A|#|#|#|
                                 |A|C|H|A|E|A|#|R|E|A|C|T|S|
                                 |C|H|A|D|#|B|A|A|#|N|I|U|E|
                                 |T|I|D|E|#|R|I|N|#|E|A|L|E|
                                 |A|Z|E|D|#|A|R|T|#|N|O|E|S|
                                 """,
                result);
    }

    /*
     * This takes < 3s s to solve at 1 GHz - which is a bit long.
     */
    @Test
    void shaded15x15() throws InterruptedException {

        final PuzzleGrid puzzle = parse(
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

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        Assertions.assertSuccess(
                """
                                 |B|L|A|B|#|M|A|M|E|E|#|C|A|G|E|
                                 |E|O|R|L|#|A|L|A|R|Y|#|A|M|I|E|
                                 |M|O|N|A|#|S|O|R|E|E|#|S|A|N|K|
                                 |A|N|A|B|A|S|E|S|#|D|A|C|H|A|S|
                                 |#|#|#|B|I|A|S|#|B|R|N|O|#|#|#|
                                 |A|S|H|E|T|S|#|A|R|O|U|S|A|L|S|
                                 |L|A|I|R|S|#|N|E|E|P|S|#|R|A|I|
                                 |A|N|D|S|#|C|A|D|E|S|#|C|O|N|N|
                                 |N|A|E|#|C|L|A|E|S|#|C|L|A|D|E|
                                 |G|A|S|M|A|I|N|S|#|G|R|A|D|E|S|
                                 |#|#|#|I|N|N|S|#|A|R|A|R|#|#|#|
                                 |A|G|A|S|S|I|#|A|R|A|B|I|S|T|S|
                                 |B|A|B|S|#|C|A|D|E|T|#|S|A|R|I|
                                 |B|U|B|A|#|A|C|A|R|I|#|S|A|I|S|
                                 |S|L|A|Y|#|L|E|R|E|S|#|A|B|E|T|
                                 """,
                result);
    }
}
