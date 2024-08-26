/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.sat;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import org.junit.jupiter.api.Test;

/** Tests for {@link Solver} with small grids and small word lists. */
final class SolverSimpleTest {

    @Test
    void empty() throws InterruptedException {
        final char[][] inputGrid = new char[][] {};
        final String[] words = new String[0];

        final Solver.Result result = new Solver(inputGrid, words).solve();

        final var output = assertInstanceOf(Solver.Result.Sat.class, result);
        assertArrayEquals(inputGrid, output.grid());
    }

    @Test
    void solve_trivial() throws InterruptedException {
        final char[][] inputGrid = new char[][] {
            {'.', '.', '.'},
            {'.', '.', '.'},
            {'.', '.', '.'}
        };
        final String[] words = new String[] {"AAA", "BBB", "CDE", "ABC", "ABD", "ABE"};

        final Solver.Result result = new Solver(inputGrid, words).solve();

        // Solver doesn't try to avoid duplicates for now
        final var output = assertInstanceOf(Solver.Result.Sat.class, result);
        final char[][] expectedGrid = new char[][] {
            {'B', 'B', 'B'},
            {'B', 'B', 'B'},
            {'B', 'B', 'B'}
        };
        assertArrayEquals(expectedGrid, output.grid());
    }

    @Test
    void solve_partiallyPrefilled_1x3() throws InterruptedException {
        final char[][] inputGrid = new char[][] {
            {'A', 'B', '.'},
        };
        final String[] words = new String[] {"ABC"};

        final Solver.Result result = new Solver(inputGrid, words).solve();

        final var output = assertInstanceOf(Solver.Result.Sat.class, result);
        final char[][] expectedGrid = new char[][] {{'A', 'B', 'C'}};
        assertArrayEquals(expectedGrid, output.grid());
    }

    @Test
    void solve_partiallyPrefilled_3x3() throws InterruptedException {
        final char[][] inputGrid = new char[][] {
            {'A', 'B', 'C'},
            {'.', '.', '.'},
            {'.', '.', '.'}
        };
        final String[] words = new String[] {"AAA", "BBB", "CDE", "ABC", "ABD", "ABE"};

        final Solver.Result result = new Solver(inputGrid, words).solve();

        final var output = assertInstanceOf(Solver.Result.Sat.class, result);
        final char[][] expectedGrid = new char[][] {
            {'A', 'B', 'C'},
            {'A', 'B', 'D'},
            {'A', 'B', 'E'}
        };
        assertArrayEquals(expectedGrid, output.grid());
    }

    @Test
    void solve_withBlocks() throws InterruptedException {
        final char[][] inputGrid = new char[][] {
            {'A', 'B', 'C'},
            {'.', '.', '#'},
            {'#', '.', '.'}
        };
        final String[] words = new String[] {"AA", "BBB", "ABC", "AB", "BE"};

        final Solver.Result result = new Solver(inputGrid, words).solve();

        final var output = assertInstanceOf(Solver.Result.Sat.class, result);
        final char[][] expectedGrid = new char[][] {
            {'A', 'B', 'C'},
            {'A', 'B', '#'},
            {'#', 'B', 'E'}
        };
        assertArrayEquals(expectedGrid, output.grid());
    }

    @Test
    void solve_impossible_noSolution() throws InterruptedException {
        final char[][] inputGrid = new char[][] {
            {'A', 'B', 'C'},
            {'.', '.', '.'},
            {'.', '.', '.'}
        };
        final String[] words = new String[] {"AAA", "BBB", "CDF" /* should be CDE */, "ABC", "ABD", "ABE"};

        final Solver.Result result = new Solver(inputGrid, words).solve();

        final var output = assertInstanceOf(Solver.Result.Unsat.class, result);
        assertEquals(Set.of(new Pos(1, 0), new Pos(2, 0)), output.nonAssignablePositions());
    }

    @Test
    void solve_impossible_noCandidate() throws InterruptedException {
        final char[][] inputGrid = new char[][] {
            {'.', '.', '.'},
            {'.', '.', '.'},
            {'.', '.', '.'}
        };
        final String[] words = new String[0];

        final Solver.Result result = new Solver(inputGrid, words).solve();

        final var output = assertInstanceOf(Solver.Result.Unsat.class, result);
        assertEquals(Set.of(), output.nonAssignablePositions());
    }
}
