/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.common.puzzle.PuzzleGridParser;

/** Tests on some asymmetric French grids. */
final class CrosswordSolverAsymmetricGridTest {

    /** The test dictionary. */
    private static Dictionary dictionary;

    @BeforeAll
    static void beforeAll() throws IOException, URISyntaxException {
        final URL dictionaryUrl = Objects.requireNonNull(
                CrosswordSolverAsymmetricGridTest.class.getResource("/UKACD18plus.txt"),
                "Test dictionary not found, verify the test resources.");
        final Path dictionaryPath = Path.of(dictionaryUrl.toURI());
        dictionary = new DictionaryMock(dictionaryPath);
    }

    @AfterAll
    static void afterAll() {
        dictionary = null;
    }

    @Test
    void shaded6x4() throws InterruptedException {
        final PuzzleGrid puzzle = PuzzleGridParser.parse(
                """
                | | | | |#| |
                | | |#| | | |
                | | |#| | | |
                | | |#| | | |
                """);

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        Assertions.assertSuccess(
                """
                |S|H|A|T|#|H|
                |A|A|#|A|I|A|
                |A|R|#|A|R|E|
                |B|E|#|L|A|D|
                """,
                result);
    }

    @Test
    void shaded12x10() throws InterruptedException {
        final PuzzleGrid puzzle = PuzzleGridParser.parse(
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

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        Assertions.assertSuccess(
                """
                |O|B|T|E|S|T|A|T|I|O|N|S|
                |P|E|A|N|#|A|R|B|O|R|E|T|
                |S|L|I|C|K|S|T|O|N|E|#|R|
                |O|L|L|A|#|S|E|N|#|S|R|I|
                |M|A|L|M|#|E|R|E|C|T|E|D|
                |A|D|E|P|T|L|Y|#|H|E|T|E|
                |N|O|S|E|R|S|#|M|A|S|#|W|
                |I|N|#|D|I|#|R|A|S|#|F|A|
                |A|N|A|#|B|A|A|S|#|A|R|Y|
                |C|A|R|P|E|N|T|A|R|I|A|S|
                """,
                result);
    }
}
