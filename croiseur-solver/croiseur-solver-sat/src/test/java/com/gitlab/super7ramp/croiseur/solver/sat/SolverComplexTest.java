/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.sat;

import com.gitlab.super7ramp.croiseur.dictionary.common.StringFilters;
import com.gitlab.super7ramp.croiseur.dictionary.common.StringTransformers;
import org.junit.jupiter.api.BeforeAll;
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
                         .filter(StringFilters.isAscii())
                         .toArray(String[]::new);
        }
    }

    // < 1s at 1GHz
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

    // ~ 5s at 1GHz
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
                {'P', 'E', 'R', 'V'},
                {'H', 'U', 'I', 'A'},
                {'U', 'R', 'E', 'A'},
                {'T', 'O', 'L', 'L'}
        };
        assertArrayEquals(expectedGrid, outputGrid);
    }

    // ~6s at 1GHz
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
                {'D', 'I', 'R', 'A', 'C'},
                {'#', 'D', 'O', 'C', '#'},
                {'#', '#', 'N', '#', '#'}
        };
        assertArrayEquals(expectedGrid, outputGrid);
    }

    // ~16s at 1GHz
    @Test
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
                {'#', '#', '#', 'Z', 'I', 'A', '#', '#', '#'},
                {'#', '#', 'B', 'E', 'A', 'N', 'S', '#', '#'},
                {'#', 'T', 'A', 'N', 'N', 'A', 'T', 'E', '#'},
                {'H', 'O', 'B', 'O', '#', 'N', 'A', 'J', 'A'},
                {'A', 'R', 'A', '#', '#', '#', 'W', 'E', 'B'},
                {'N', 'I', 'S', 'I', '#', 'M', 'I', 'C', 'A'},
                {'#', 'I', 'S', 'S', 'U', 'A', 'N', 'T', '#'},
                {'#', '#', 'U', 'N', 'D', 'U', 'G', '#', '#'},
                {'#', '#', '#', 'T', 'O', 'N', '#', '#', '#'},
        };
        assertArrayEquals(expectedGrid, outputGrid);
    }

    // ~48s at 1GHz
    @Test
    void shaded13x13() {
        final char[][] inputGrid = new char[][]{
                {'.', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.'},
                {'#', '#', '#', '.', '.', '.', '#', '.', '.', '.', '#', '#', '#'},
                {'.', '.', '.', '.', '.', '#', '#', '#', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '#', '#', '#', '#', '#', '#', '#', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '#', '#', '#', '.', '.', '.', '.', '.'},
                {'#', '#', '#', '.', '.', '.', '#', '.', '.', '.', '#', '#', '#'},
                {'.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '.'},
        };

        final char[][] outputGrid = new Solver(inputGrid, words).solve();

        final char[][] expectedGrid = new char[][]{
                {'S', 'C', 'A', 'T', '#', 'G', 'O', 'A', '#', 'O', 'A', 'H', 'U'},
                {'E', 'I', 'R', 'E', '#', 'R', 'O', 'Z', '#', 'X', 'M', 'A', 'S'},
                {'E', 'A', 'C', 'H', '#', 'A', 'N', 'T', '#', 'T', 'Y', 'R', 'E'},
                {'M', 'O', 'H', 'E', 'L', 'S', '#', 'E', 'Y', 'E', 'L', 'I', 'D'},
                {'#', '#', '#', 'E', 'A', 'S', '#', 'C', 'O', 'R', '#', '#', '#'},
                {'S', 'N', 'A', 'S', 'H', '#', '#', '#', 'B', 'S', 'I', 'D', 'E'},
                {'O', 'E', 'R', '#', '#', '#', '#', '#', '#', '#', 'T', 'O', 'M'},
                {'B', 'O', 'M', 'B', 'O', '#', '#', '#', 'C', 'Z', 'A', 'R', 'S'},
                {'#', '#', '#', 'A', 'B', 'A', '#', 'J', 'U', 'S', '#', '#', '#'},
                {'U', 'J', 'A', 'M', 'A', 'A', '#', 'A', 'Z', 'A', 'L', 'E', 'A'},
                {'M', 'A', 'R', 'A', '#', 'R', 'U', 'C', '#', 'Z', 'I', 'M', 'B'},
                {'B', 'I', 'L', 'K', '#', 'O', 'N', 'O', '#', 'S', 'K', 'U', 'A'},
                {'O', 'L', 'E', 'O', '#', 'N', 'A', 'B', '#', 'A', 'E', 'S', 'C'},
        };
        assertArrayEquals(expectedGrid, outputGrid);
    }
}
