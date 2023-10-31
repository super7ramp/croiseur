/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.sat;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * The definition of a group of contiguous cells.
 */
final class Slot {

    /** The minimal length of a slot. */
    static final int MIN_LENGTH = 2;

    /** The index of this slot in the grid's list of slots. */
    private final int index;

    /** The start of the varying coordinate. */
    private final int start;

    /** The end of the varying coordinate. */
    private final int end;

    /** The fixed coordinate. */
    private final int offset;

    /** Whether this a down slot (= vertical). */
    private final boolean isDown;

    /**
     * Constructs an instance.
     *
     * @param indexArg  the index of this slot in the grid list of slots
     * @param startArg  the start of the varying coordinate (start row for a down slot, start column
     *                  for an across slot)
     * @param endArg    the end (exclusive) of the varying coordinate (end row for a down slot, end
     *                  column for an across slot)
     * @param offsetArg the fixed coordinate
     * @param isDownArg whether this is a down slot (= vertical)
     * @throws IllegalArgumentException if length is less than {@value #MIN_LENGTH}
     */
    Slot(final int indexArg, final int startArg, final int endArg, final int offsetArg,
         final boolean isDownArg) {
        if (endArg - startArg < MIN_LENGTH) {
            // Technically this shouldn't happen since it is a purely internal class called by
            // another purely internal and hopefully bug-free class. Let's make this class
            // self-consistent though, in case the library API changes.
            throw new IllegalArgumentException(
                    "Invalid slot length: Slot must be at least " + MIN_LENGTH +
                    " cells-long, given slot length is " + (endArg - startArg));
        }
        index = indexArg;
        start = startArg;
        end = endArg;
        offset = offsetArg;
        isDown = isDownArg;
    }

    /**
     * Constructs an across slot.
     *
     * @param index       the index of this slot in the grid list of slots
     * @param startColumn the start column
     * @param endColumn   the end column
     * @param row         the row
     */
    static Slot across(final int index, final int startColumn, final int endColumn,
                       final int row) {
        return new Slot(index, startColumn, endColumn, row, false);
    }

    /**
     * Constructs a down slot.
     *
     * @param index    the index of this slot in the grid list of slots
     * @param startRow the start row
     * @param endRow   the end row
     * @param column   the column
     */
    static Slot down(final int index, final int startRow, final int endRow, final int column) {
        return new Slot(index, startRow, endRow, column, true);
    }

    /**
     * The positions of the cells of this slot.
     *
     * @return positions of the cells of this slot
     */
    List<Pos> positions() {
        return IntStream.range(start, end)
                        .mapToObj(i -> isDown ? new Pos(offset, i) : new Pos(i, offset))
                        .toList();
    }

    /**
     * The length of this slot.
     *
     * @return the length of this slot
     */
    int length() {
        return end - start;
    }

    /**
     * The index of this slot in the grid list of slots.
     *
     * @return the index of this slot in the grid list of slots
     */
    int index() {
        return index;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Slot slot)) return false;
        return index == slot.index && start == slot.start && end == slot.end &&
               offset == slot.offset &&
               isDown == slot.isDown;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, start, end, offset, isDown);
    }

    @Override
    public String toString() {
        return "Slot{" +
               "index=" + index +
               ", start=" + start +
               ", end=" + end +
               ", offset=" + offset +
               ", isDown=" + isDown +
               '}';
    }
}