/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.sat;

import com.gitlab.super7ramp.croiseur.dictionary.common.StringTransformers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Tests for {@link Solver} with large grids and/or large word lists.
 */
final class SolverComplexTest {

    /** The test word list. */
    private static String[] words;

    /**
     * Sets up the word list for all tests in this class.
     *
     * @throws NullPointerException if test dictionary is not found
     * @throws URISyntaxException   if test dictionary location fails to be converted to URI
     * @throws IOException          if test dictionary fails to be read
     */
    @BeforeAll
    static void setUp() throws URISyntaxException, IOException {
        final var dictionaryUrl =
                Objects.requireNonNull(SolverComplexTest.class.getResource("/UKACD18plus.txt"),
                                       "Test dictionary not found, verify the test resources.");
        final var dictionaryPath = Path.of(dictionaryUrl.toURI());
        try (final Stream<String> lines = Files.lines(dictionaryPath)) {
            words = lines.map(StringTransformers.toAcceptableCrosswordEntry())
                         .toArray(String[]::new);
        }
    }

    @Test
    void empty3x3() {
        final char[][] inputGrid = new char[][]{
                {'.', '.', '.'},
                {'.', '.', '.'},
                {'.', '.', '.'}
        };

        final char[][] outputGrid = new Solver(inputGrid, words).solve();

        final char[][] expectedGrid = new char[][]{
                {'B', 'A', 'A'},
                {'A', 'B', 'B'},
                {'B', 'A', 'A'}
        };
        assertArrayEquals(expectedGrid, outputGrid);
    }

    @Test
    void empty4x4() {
        final char[][] inputGrid = new char[][]{
                {'.', '.', '.', '.'},
                {'.', '.', '.', '.'},
                {'.', '.', '.', '.'},
                {'.', '.', '.', '.'}
        };

        final char[][] outputGrid = new Solver(inputGrid, words).solve();

        final char[][] expectedGrid = new char[][]{
                {'S', 'K', 'U', 'A'},
                {'K', 'E', 'R', 'B'},
                {'U', 'R', 'E', 'A'},
                {'A', 'B', 'A', 'C'}
        };
        assertArrayEquals(expectedGrid, outputGrid);
    }

    @Test
    void shaded5x5() {
        final char[][] inputGrid = new char[][]{
                {'#', '#', '.', '.', '#'},
                {'#', '.', '.', '.', '#'},
                {'.', '.', '.', '.', '.'},
                {'#', '.', '.', '.', '#'},
                {'#', '#', '.', '#', '#'}
        };

        final char[][] outputGrid = new Solver(inputGrid, words).solve();

        final char[][] expectedGrid = new char[][]{
                {'#', '#', 'A', 'A', '#'},
                {'#', 'T', 'A', 'B', '#'},
                {'K', 'I', 'R', 'A', 'N'},
                {'#', 'D', 'O', 'C', '#'},
                {'#', '#', 'N', '#', '#'}
        };
        assertArrayEquals(expectedGrid, outputGrid);
    }

    @Test
    @Disabled("memory explosion")
    void shaded9x9() {
        final char[][] inputGrid = new char[][]{
                {'#', '#', '#', '.', '.', '.', '#', '#', '#'},
                {'#', '#', '.', '.', '.', '.', '.', '#', '#'},
                {'#', '.', '.', '.', '.', '.', '.', '.', '#'},
                {'.', '.', '.', '.', '#', '.', '.', '.', '.'},
                {'.', '.', '.', '#', '#', '#', '.', '.', '.'},
                {'.', '.', '.', '.', '#', '.', '.', '.', '.'},
                {'#', '.', '.', '.', '.', '.', '.', '.', '#'},
                {'#', '#', '.', '.', '.', '.', '.', '#', '#'},
                {'#', '#', '#', '.', '.', '.', '#', '#', '#'},
        };

        final char[][] outputGrid = new Solver(inputGrid, words).solve();

        final char[][] expectedGrid = new char[][]{
                {'#', '#', '#', '.', '.', '.', '#', '#', '#'},
                {'#', '#', '.', '.', '.', '.', '.', '#', '#'},
                {'#', '.', '.', '.', '.', '.', '.', '.', '#'},
                {'.', '.', '.', '.', '#', '.', '.', '.', '.'},
                {'.', '.', '.', '#', '#', '#', '.', '.', '.'},
                {'.', '.', '.', '.', '#', '.', '.', '.', '.'},
                {'#', '.', '.', '.', '.', '.', '.', '.', '#'},
                {'#', '#', '.', '.', '.', '.', '.', '#', '#'},
                {'#', '#', '#', '.', '.', '.', '#', '#', '#'},
        };
        assertArrayEquals(expectedGrid, outputGrid);
    }
}
