/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg;

import com.gitlab.super7ramp.croiseur.common.PuzzleDefinition;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

import static com.gitlab.super7ramp.croiseur.solver.ginsberg.PuzzleDefinitionParser.parsePuzzle;

/**
 * Tests on classic Anglo-Saxon symmetric grids.
 */
final class CrosswordSolverSymmetricGridTest {

    /** The test dictionary. */
    private static Dictionary dictionary;

    @BeforeAll
    static void beforeAll() throws IOException, URISyntaxException {
        final URL dicUrl =
                Objects.requireNonNull(CrosswordSolverSymmetricGridTest.class.getResource("/fr" +
                        ".dic"));
        final Path dicPath = Path.of(dicUrl.toURI());
        dictionary = new DictionaryMock(dicPath);
    }

    @AfterAll
    static void afterAll() {
        dictionary = null;
    }

    /*
     * This takes < 2 s to solve at 1 GHz.
     */
    @Test
    void shaded5x5() throws InterruptedException {
        final PuzzleDefinition puzzle = parsePuzzle("""
                |#|#| | | |
                |#| | | | |
                | | | | | |
                | | | | |#|
                | | | |#|#|
                """);

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        Assertions.assertSuccess("""
                |#|#|C|O|L|
                |#|R|A|T|E|
                |A|G|L|A|E|
                |B|A|I|N|#|
                |C|A|N|#|#|
                """, result);
    }

    /*
     * This takes < 2 s to solve at 1 GHz. No backtrack necessary.
     */
    @Test
    void shaded9x9() throws InterruptedException {
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

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        Assertions.assertSuccess("""
                |#|#|#|R|G|I|#|#|#|
                |#|#|M|A|T|E|R|#|#|
                |#|D|O|L|O|R|E|S|#|
                |A|I|D|E|#|S|P|E|T|
                |B|A|I|#|#|#|E|R|E|
                |A|N|S|E|#|A|N|T|E|
                |#|A|T|L|A|N|T|E|#|
                |#|#|E|S|S|A|I|#|#|
                |#|#|#|A|I|R|#|#|#|
                """, result);
    }

    /*
     * This takes < 3s to solve at 1 GHz.
     */
    @Test
    void shaded13x13() throws InterruptedException {
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

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        Assertions.assertSuccess("""
                |T|A|R|A|#|C|R|E|#|A|L|E|P|
                |A|R|E|C|#|A|I|R|#|D|E|L|L|
                |T|E|T|E|#|R|A|I|#|O|H|I|O|
                |A|S|T|R|A|L|#|C|A|N|N|E|T|
                |#|#|#|B|I|O|#|A|O|I|#|#|#|
                |A|B|I|E|S|#|#|#|I|S|A|R|D|
                |B|A|I|#|#|#|#|#|#|#|R|A|I|
                |A|L|I|A|S|#|#|#|C|H|A|S|E|
                |#|#|#|B|A|C|#|E|I|O|#|#|#|
                |A|L|T|A|I|R|#|P|A|C|S|E|R|
                |M|E|A|T|#|A|R|A|#|H|A|L|O|
                |O|G|R|E|#|I|I|I|#|E|D|A|M|
                |S|E|N|E|#|G|S|S|#|R|E|N|E|
                """, result);
    }

    /*
     * This takes < 3s to solve at 1 GHz.
     */
    @Test
    void shaded13x13WithLongWords() throws InterruptedException {
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

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        Assertions.assertSuccess("""
                |S|A|I|D|#|V|I|S|#|P|E|R|L|
                |A|R|M|E|#|I|E|L|#|E|P|E|E|
                |N|U|E|R|#|I|R|A|#|R|E|I|N|
                |A|M|I|N|C|I|S|S|E|M|E|N|T|
                |#|#|#|I|I|E|#|H|I|E|#|#|#|
                |A|B|C|E|S|#|#|#|B|A|G|O|U|
                |B|A|A|R|#|#|#|#|#|B|A|A|R|
                |A|L|L|E|R|#|#|#|B|I|L|I|E|
                |#|#|#|S|A|C|#|M|A|L|#|#|#|
                |I|D|E|N|T|I|T|A|R|I|S|M|E|
                |M|A|L|E|#|R|A|Z|#|S|O|A|P|
                |E|L|B|E|#|A|I|D|#|E|U|R|E|
                |I|I|E|S|#|D|E|A|#|R|I|X|E|
                 """, result);
    }

    /*
     * This takes < 6 s to solve at 1 GHz - which is a bit long.
     */
    @Test
    void shaded15x15() throws InterruptedException {

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

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        Assertions.assertSuccess("""
                |A|B|L|E|#|C|R|A|I|G|#|A|R|M|E|
                |I|L|O|T|#|R|E|C|R|E|#|R|A|U|L|
                |D|O|N|A|#|A|N|T|A|N|#|A|M|E|L|
                |A|G|E|L|A|S|T|E|#|T|A|C|I|T|E|
                |#|#|#|A|I|S|E|#|A|I|L|E|#|#|#|
                |A|L|E|G|R|E|#|A|N|A|P|E|S|T|E|
                |G|A|L|E|E|#|P|I|G|N|E|#|T|A|U|
                |N|U|E|R|#|M|A|G|I|E|#|S|I|L|L|
                |U|R|I|#|M|E|T|R|E|#|B|I|L|L|E|
                |S|E|S|B|A|N|I|E|#|B|O|M|B|E|R|
                |#|#|#|E|R|I|N|#|A|U|R|A|#|#|#|
                |C|A|L|A|I|S|#|A|R|T|E|R|I|E|L|
                |A|V|E|U|#|C|A|L|O|T|#|U|N|A|U|
                |L|A|O|N|#|A|B|I|M|E|#|B|R|R|R|
                |O|R|N|E|#|L|A|M|E|R|#|A|I|L|E|
                """, result);
    }
}
