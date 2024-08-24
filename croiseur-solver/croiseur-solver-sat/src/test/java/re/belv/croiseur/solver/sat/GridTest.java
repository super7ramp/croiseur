/*
 * SPDX-FileCopyrightText: 2024 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.sat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Tests on {@link Grid}. */
final class GridTest {

    @Test
    void newGrid_null() {
        assertThrows(NullPointerException.class, () -> new Grid(null));
    }

    @Test
    void newGrid_inconsistentLength() {
        final char[][] cells = new char[][] {
            {'A', 'B', 'C'},
            {'.', '#'}
        };
        final var exception = assertThrows(IllegalArgumentException.class, () -> new Grid(cells));
        assertEquals("Inconsistent number of columns: Row #1 has 2 columns but row #0 has 3", exception.getMessage());
    }

    @Test
    void newGrid_invalidLetter() {
        final char[][] cells = new char[][] {
            {'A', 'B', 'C'},
            {'.', '#', '@'}
        };
        final var exception = assertThrows(IllegalArgumentException.class, () -> new Grid(cells));
        assertEquals("Invalid value at row #1, column #2: @", exception.getMessage());
    }

    @Test
    void rowCount() {
        final char[][] cells = new char[][] {{'A'}, {'B'}};
        final int numberOfRows = new Grid(cells).rowCount();
        assertEquals(2, numberOfRows);
    }

    @Test
    void columnCount() {
        final char[][] cells = new char[][] {{'A'}, {'B'}};
        final int numberOfColumns = new Grid(cells).columnCount();
        assertEquals(1, numberOfColumns);
    }

    @Test
    void letterAt() {
        final char[][] cells = new char[][] {
            {'A', 'B'},
            {'.', '#'}
        };
        final char letter = new Grid(cells).letterAt(0, 1);
        assertEquals('B', letter);
    }

    @Test
    void slots_simple() {
        final char[][] cells = new char[][] {
            {'.', '.', '.'},
            {'.', '.', '.'},
            {'.', '.', '.'}
        };

        final List<Slot> slots = new Grid(cells).slots();

        final List<Slot> expectedSlots = List.of(
                Slot.across(0, 0, 3, 0),
                Slot.across(1, 0, 3, 1),
                Slot.across(2, 0, 3, 2),
                Slot.down(3, 0, 3, 0),
                Slot.down(4, 0, 3, 1),
                Slot.down(5, 0, 3, 2));
        assertEquals(expectedSlots, slots);
    }

    @Test
    void slots_asymmetrical() {
        final char[][] cells = new char[][] {
            {'.', '.', '.'},
            {'.', '.', '.'}
        };

        final List<Slot> slots = new Grid(cells).slots();

        final List<Slot> expectedSlots = List.of(
                Slot.across(0, 0, 3, 0),
                Slot.across(1, 0, 3, 1),
                Slot.down(2, 0, 2, 0),
                Slot.down(3, 0, 2, 1),
                Slot.down(4, 0, 2, 2));
        assertEquals(expectedSlots, slots);
    }

    @Test
    void slots_withBlocks() {
        final char[][] cells = new char[][] {
            {'.', '#', '.'},
            {'.', '.', '.'},
            {'.', '.', '#'}
        };

        final List<Slot> slots = new Grid(cells).slots();

        final List<Slot> expectedSlots = List.of(
                Slot.across(0, 0, 3, 1),
                Slot.across(1, 0, 2, 2),
                Slot.down(2, 0, 3, 0),
                Slot.down(3, 1, 3, 1),
                Slot.down(4, 0, 2, 2));
        assertEquals(expectedSlots, slots);
    }

    @Test
    void slots_empty() {
        final char[][] cells = new char[0][0];
        final List<Slot> slots = new Grid(cells).slots();
        assertEquals(Collections.emptyList(), slots);
    }
}
