/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.paulgb.plugin;

import com.gitlab.super7ramp.croiseur.common.GridPosition;
import com.gitlab.super7ramp.croiseur.common.PuzzleDefinition;
import com.gitlab.super7ramp.croiseur.solver.paulgb.Puzzle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link PuzzleAdapter}.
 */
final class PuzzleAdapterTest {

    /**
     * Simple symmetric grid, no shaded box, no pre-filled box.
     */
    @Test
    void symmetric() {
        final PuzzleDefinition puzzle = new PuzzleDefinition(4, 4, Collections.emptySet(),
                Collections.emptyMap());
        final NumberedPuzzleDefinition numberedPuzzle = new NumberedPuzzleDefinition(puzzle);

        final Puzzle emptyGrid = PuzzleAdapter.adapt(numberedPuzzle);

        assertEquals(8, emptyGrid.slots().length);
        final int[][] expectedSlots = {
                // horizontal slots
                {0, 1, 2, 3},
                {4, 5, 6, 7},
                {8, 9, 10, 11},
                {12, 13, 14, 15},
                // vertical slots
                {0, 4, 8, 12},
                {1, 5, 9, 13},
                {2, 6, 10, 14},
                {3, 7, 11, 15}
        };
        assertArrayEquals(expectedSlots, emptyGrid.slots());
    }

    /**
     * Simple asymmetric grid, no shaded box, no pre-filled box.
     */
    @Test
    void asymmetric() {
        final PuzzleDefinition puzzle = new PuzzleDefinition(3, 5, Collections.emptySet(),
                Collections.emptyMap());
        final NumberedPuzzleDefinition numberedPuzzle = new NumberedPuzzleDefinition(puzzle);

        final Puzzle emptyGrid = PuzzleAdapter.adapt(numberedPuzzle);

        assertEquals(8, emptyGrid.slots().length);
        final int[][] expectedSlots = {
                // horizontal slots
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8},
                {9, 10, 11},
                {12, 13, 14},
                // vertical slots
                {0, 3, 6, 9, 12},
                {1, 4, 7, 10, 13},
                {2, 5, 8, 11, 14}
        };
        assertArrayEquals(expectedSlots, emptyGrid.slots());
    }

    /**
     * Symmetric grid with shaded boxes.
     * <pre>
     *     | #| 0| 1| 2|
     *     | 3| 4| 5| 6|
     *     | 7| 8| #| 9|
     *     |10|11|12|13|
     * </pre>
     */
    @Test
    void shaded() {

        final PuzzleDefinition puzzle =
                new PuzzleDefinition.PuzzleDefinitionBuilder().width(4)
                                                              .height(4)
                                                              .shade(new GridPosition(0, 0))
                                                              .shade(new GridPosition(2, 2))
                                                              .build();
        final NumberedPuzzleDefinition numberedPuzzle = new NumberedPuzzleDefinition(puzzle);

        final Puzzle emptyGrid = PuzzleAdapter.adapt(numberedPuzzle);

        assertEquals(8, emptyGrid.slots().length);
        final int[][] expectedSlots = {
                // horizontal slots
                {0, 1, 2},
                {3, 4, 5, 6},
                {7, 8}, /* {9} is only 1 letter long, it's not really a horizontal slot. */
                {10, 11, 12, 13},
                // vertical slots
                {3, 7, 10},
                {0, 4, 8, 11},
                {1, 5}, /* {12} is only 1 letter long, it's not really a vertical slot. */
                {2, 6, 9, 13}
        };
        assertArrayEquals(expectedSlots, emptyGrid.slots());
    }

    /**
     * Crossword Composer doesn't support partially pre-filled grid. An exception should be raised
     * when a pre-filled grid is given.
     */
    @Test
    void prefilled() {
        final PuzzleDefinition puzzle = new PuzzleDefinition(4, 4, Collections.emptySet(),
                Collections.singletonMap(new GridPosition(0, 0), 'A'));
        final NumberedPuzzleDefinition numberedPuzzle = new NumberedPuzzleDefinition(puzzle);

        final Executable call = () -> PuzzleAdapter.adapt(numberedPuzzle);

        final Throwable e = assertThrows(UnsupportedOperationException.class, call);
        assertEquals("paulgb's solver doesn't support pre-filled grid", e.getMessage());
    }
}
