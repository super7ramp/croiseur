/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.sat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A crossword grid.
 */
final class Grid {

    /** The character representing a block, i.e. a shaded cell. */
    static final char BLOCK = '#';

    /** The character representing an empty cell. */
    static final char EMPTY = '.';

    /** The raw cells. */
    private final char[][] cells;

    /** The slots. */
    private final List<Slot> slots;

    /**
     * Constructs an instance.
     *
     * @param cellsArg the row cells
     * @throws NullPointerException     if given cell array is {@code null}
     * @throws IllegalArgumentException if given cell array is non-{@code null} but invalid
     */
    Grid(final char[][] cellsArg) {
        cells = validateGrid(cellsArg);
        slots = slotsFrom(cellsArg);
    }

    /**
     * Validates the given cells.
     *
     * @param cells the cells to validate
     * @return the given cells
     * @throws NullPointerException     if given cell array is {@code null}
     * @throws IllegalArgumentException if given cell array is non-{@code null} but invalid
     */
    private static char[][] validateGrid(final char[][] cells) {
        Objects.requireNonNull(cells);
        if (cells.length == 0) {
            // Trivial case, empty grid
            return cells;
        }
        final int firstRowNumberOfColumns = cells[0].length;
        for (int row = 0; row < cells.length; row++) {
            final int numberOfColumns = cells[row].length;
            if (numberOfColumns != firstRowNumberOfColumns) {
                throw new IllegalArgumentException(
                        "Inconsistent number of columns: Row #" + row + " has " + numberOfColumns +
                        " columns but row #0 has " + firstRowNumberOfColumns);
            }
            for (int column = 0; column < numberOfColumns; column++) {
                final char value = cells[row][column];
                if (value != EMPTY && value != BLOCK && !Alphabet.contains(value)) {
                    throw new IllegalArgumentException(
                            "Invalid value at row #" + row + ", column #" + column + ": " + value);
                }
            }
        }
        return cells;
    }

    /**
     * Computes the slots from the cells.
     *
     * @param cells the cells
     * @return the slots
     */
    private static List<Slot> slotsFrom(final char[][] cells) {
        final List<Slot> acrossSlots = acrossSlotsFrom(cells);
        final List<Slot> downSlots = downSlotsFrom(acrossSlots.size(), cells);
        return Stream.concat(acrossSlots.stream(), downSlots.stream()).toList();
    }

    /**
     * Computes the across slots from the cells.
     *
     * @param cells the cells
     * @return the across slots
     */
    private static List<Slot> acrossSlotsFrom(final char[][] cells) {
        final List<Slot> slots = new ArrayList<>();
        final int numberOfRows = cells.length;
        final int numberOfColumns = cells.length > 0 ? cells[0].length : 0;
        for (int row = 0; row < numberOfRows; row++) {
            int columnStart = 0;
            for (int column = columnStart; column < numberOfColumns; column++) {
                if (cells[row][column] == BLOCK) {
                    if (column - columnStart >= Slot.MIN_LENGTH) {
                        slots.add(Slot.across(slots.size(), columnStart, column, row));
                    }
                    columnStart = column + 1;
                }
            }
            if (numberOfColumns - columnStart >= Slot.MIN_LENGTH) {
                slots.add(Slot.across(slots.size(), columnStart, numberOfColumns, row));
            }
        }
        return slots;
    }

    /**
     * Computes the down slots from the cells.
     *
     * @param startIndex the start index, i.e. the number of across slots (down slots are placed
     *                   after the across slots in the slot list)
     * @param cells      the cells
     * @return the across slots
     */
    private static List<Slot> downSlotsFrom(final int startIndex, final char[][] cells) {
        final List<Slot> slots = new ArrayList<>();
        final int numberOfRows = cells.length;
        final int numberOfColumns = cells.length > 0 ? cells[0].length : 0;
        for (int column = 0; column < numberOfColumns; column++) {
            int rowStart = 0;
            for (int row = rowStart; row < numberOfRows; row++) {
                if (cells[row][column] == BLOCK) {
                    if (row - rowStart >= Slot.MIN_LENGTH) {
                        slots.add(Slot.down(startIndex + slots.size(), rowStart, row, column));
                    }
                    rowStart = row + 1;
                }
            }
            if (numberOfRows - rowStart >= Slot.MIN_LENGTH) {
                slots.add(Slot.down(startIndex + slots.size(), rowStart, numberOfRows, column));
            }
        }
        return slots;
    }

    /**
     * Returns the letter at given position.
     * <p>
     * Special character '{@value BLOCK}' is returned if the cell contains a block. Special
     * character '{@value EMPTY}' is returned if the cell contains no value.
     *
     * @param row    the cell row
     * @param column the cell column
     * @return the letter at given position
     * @throws IndexOutOfBoundsException if given position is outside grid
     */
    char letterAt(final int row, final int column) {
        return cells[row][column];
    }

    /**
     * The number of rows of the grid.
     *
     * @return the number of rows of the grid
     */
    int numberOfRows() {
        return cells.length;
    }

    /**
     * The number of columns of the grid.
     *
     * @return the number of columns of the grid
     */
    int numberOfColumns() {
        return cells.length == 0 ? 0 : cells[0].length;
    }

    /**
     * The slots of this grid, including one-letter-long slots.
     *
     * @return the slots of this grid
     */
    List<Slot> slots() {
        return slots;
    }

    /**
     * The number of slots.
     *
     * @return the number of slots
     */
    int numberOfSlots() {
        return slots.size();
    }

    @Override
    public String toString() {
        return "Grid{" +
               "cells=" + Arrays.deepToString(cells) +
               '}';
    }
}
