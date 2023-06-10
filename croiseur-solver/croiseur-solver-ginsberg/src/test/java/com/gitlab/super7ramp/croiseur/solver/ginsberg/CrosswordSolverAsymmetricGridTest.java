/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg;

import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Tests on some asymmetric French grids.
 */
final class CrosswordSolverAsymmetricGridTest {

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

    @Test
    void shaded6x4() throws InterruptedException {
        final PuzzleGrid puzzle = PuzzleGridParser.parse("""
                | | | | |#| |
                | | |#| | | |
                | | |#| | | |
                | | |#| | | |
                """);

        final SolverResult result = new GinsbergCrosswordSolver().solve(puzzle, dictionary);

        Assertions.assertSuccess("""
                |S|A|R|A|#|T|
                |A|I|#|A|B|A|
                |A|N|#|B|A|I|
                |D|E|#|A|I|N|
                """, result);
    }

    @Test
    @Disabled("Solver not performant enough for now / no solution with dictionary?")
    void shaded12x10() throws InterruptedException {
        final PuzzleGrid puzzle = PuzzleGridParser.parse("""
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

        Assertions.assertSuccess("""
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
