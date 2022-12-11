package com.gitlab.super7ramp.crosswords.solver.paulgb;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link Solver}.
 */
final class SolverTest {

    /**
     * Grid is void, so result is present but the encapsulated array is empty.
     */
    @Test
    void possible0x0() {
        final Grid emptyGrid = new Grid(new long[0][0]);
        final Dictionary emptyDictionary = new Dictionary(new String[0]);

        final Optional<char[]> result = new Solver().solve(emptyGrid, emptyDictionary);

        assertFalse(result.isEmpty());
        assertEquals(0, result.get().length);
    }

    /**
     * Dictionary is empty so there is no solution.
     */
    @Test
    void impossible3x3() {
        final Grid grid = new Grid(new long[][]{
                // horizontal slots
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8},
                // vertical slots
                {0, 3, 6},
                {1, 4, 7},
                {2, 5, 8}
        });
        final Dictionary emptyDictionary = new Dictionary(new String[0]);

        final Optional<char[]> result = new Solver().solve(grid, emptyDictionary);

        assertTrue(result.isEmpty());
    }

    /**
     * Simple 3x3 with a tailored dictionary.
     */
    @Test
    void possible3x3() {
        final Grid grid = new Grid(new long[][]{
                // horizontal slots
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8},
                // vertical slots
                {0, 3, 6},
                {1, 4, 7},
                {2, 5, 8}
        });
        final Dictionary dictionary = new Dictionary(new String[]{"AAA", "BBB", "CDE", "ABC"
                , "ABD", "ABE"});

        final Optional<char[]> result = new Solver().solve(grid, dictionary);

        assertFalse(result.isEmpty());
        assertEquals(9, result.get().length);
        System.out.println("==> " + Arrays.toString(result.get()));
        assertArrayEquals(new char[]{'A', 'B', 'C', 'A', 'B', 'D', 'A', 'B', 'E'}, result.get());
    }
}
