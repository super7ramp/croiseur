/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.sat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Tests for {@link Solver} with small grids and small word lists.
 */
final class SolverSimpleTest {

    @Test
    void empty() throws InterruptedException {
        final char[][] inputGrid = new char[][]{};
        final String[] words = new String[0];

        final char[][] outputGrid = new Solver(inputGrid, words).solve();

        assertArrayEquals(inputGrid, outputGrid);
    }

    @Test
    void solve_trivial() throws InterruptedException {
        final char[][] inputGrid = new char[][]{
                {'.', '.', '.'},
                {'.', '.', '.'},
                {'.', '.', '.'}
        };
        final String[] words = new String[]{"AAA", "BBB", "CDE", "ABC", "ABD", "ABE"};

        final char[][] outputGrid = new Solver(inputGrid, words).solve();

        // Solver doesn't try to avoid duplicates for now
        final char[][] expectedGrid = new char[][]{
                {'B', 'B', 'B'},
                {'B', 'B', 'B'},
                {'B', 'B', 'B'}
        };
        assertArrayEquals(expectedGrid, outputGrid);
    }

    @Test
    void solve_partiallyPrefilled_1x3() throws InterruptedException {
        final char[][] inputGrid = new char[][]{
                {'A', 'B', '.'},
        };
        final String[] words = new String[]{"ABC"};

        final char[][] outputGrid = new Solver(inputGrid, words).solve();

        final char[][] expectedGrid = new char[][]{
                {'A', 'B', 'C'}
        };
        assertArrayEquals(expectedGrid, outputGrid);
    }

    @Test
    void solve_partiallyPrefilled_3x3() throws InterruptedException {
        final char[][] inputGrid = new char[][]{
                {'A', 'B', 'C'},
                {'.', '.', '.'},
                {'.', '.', '.'}
        };
        final String[] words = new String[]{"AAA", "BBB", "CDE", "ABC", "ABD", "ABE"};

        final char[][] outputGrid = new Solver(inputGrid, words).solve();

        final char[][] expectedGrid = new char[][]{
                {'A', 'B', 'C'},
                {'A', 'B', 'D'},
                {'A', 'B', 'E'}
        };
        assertArrayEquals(expectedGrid, outputGrid);
    }

    @Test
    void solve_withBlocks() throws InterruptedException {
        final char[][] inputGrid = new char[][]{
                {'A', 'B', 'C'},
                {'.', '.', '#'},
                {'#', '.', '.'}
        };
        final String[] words = new String[]{"AA", "BBB", "ABC", "AB", "BE"};

        final char[][] outputGrid = new Solver(inputGrid, words).solve();

        final char[][] expectedGrid = new char[][]{
                {'A', 'B', 'C'},
                {'A', 'B', '#'},
                {'#', 'B', 'E'}
        };
        assertArrayEquals(expectedGrid, outputGrid);
    }

    @Test
    void solve_impossible_noSolution() throws InterruptedException {
        final char[][] inputGrid = new char[][]{
                {'A', 'B', 'C'},
                {'.', '.', '.'},
                {'.', '.', '.'}
        };
        final String[] words =
                new String[]{"AAA", "BBB", "CDF" /* should be CDE */, "ABC", "ABD", "ABE"};

        final char[][] outputGrid = new Solver(inputGrid, words).solve();

        assertArrayEquals(new char[][]{}, outputGrid);
    }

    @Test
    void solve_impossible_noCandidate() throws InterruptedException {
        final char[][] inputGrid = new char[][]{
                {'.', '.', '.'},
                {'.', '.', '.'},
                {'.', '.', '.'}
        };
        final String[] words = new String[0];

        final char[][] outputGrid = new Solver(inputGrid, words).solve();

        assertArrayEquals(new char[][]{}, outputGrid);
    }
}