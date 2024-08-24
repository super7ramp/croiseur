/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model.slot;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import re.belv.croiseur.gui.view.model.GridCoord;

/**
 * Slot outline base class.
 *
 * <p>Example of across slot:
 *
 * <pre>
 *      0 1 2 3 4 5 6
 *   0 | | | | | | | |
 *   1 | |#|A|B|C|#| | <-- offset = 1
 *   2 | | | | | | | |
 *   3 | | | |#| | | |
 *          ^     ^
 *          |      ` end = 4 (exclusive)
 *          `- start = 2 (inclusive)
 * </pre>
 *
 * Example of down slot:
 *
 * <pre>
 *      0 1 2 3 4 5 6
 *   0 | | | |B| | | | <-- start = 0 (inclusive)
 *   1 | |#| |B| |#| |
 *   2 | | | |B| | | |
 *   3 | | | |#| | | | <-- end = 3 (exclusive)
 *            ^
 *            ` offset = 3
 * </pre>
 */
public abstract sealed class SlotOutline {

    /** The varying coordinate start index. */
    protected final int start;

    /** The varying coordinate end index. */
    protected final int end;

    /** The other coordinate fixed value. */
    protected final int offset;

    /**
     * Constructs an instance.
     *
     * @param startArg the start index
     * @param endArg the end index
     * @param offsetArg the other coordinate fixed value
     */
    protected SlotOutline(final int startArg, final int endArg, final int offsetArg) {
        start = startArg;
        end = endArg;
        offset = offsetArg;
    }

    /**
     * Creates a slot across the given column start and end indexes, at given row index.
     *
     * @param columnStart the slot column start index (inclusive)
     * @param columnEnd the slot column end index (exclusive)
     * @param row the row index
     * @return a slot across the given column start and end indexes, at given row index
     */
    public static SlotOutline across(final int columnStart, final int columnEnd, final int row) {
        return new AcrossSlotOutline(columnStart, columnEnd, row);
    }

    /**
     * Creates a slot down the given row start and end indexes, at given column index.
     *
     * @param rowStart the slot row start index (inclusive)
     * @param rowEnd the slot row end index (exclusive)
     * @param column the column index
     * @return a slot down the given row start and end indexes, at given column index
     */
    public static SlotOutline down(final int rowStart, final int rowEnd, final int column) {
        return new DownSlotOutline(rowStart, rowEnd, column);
    }

    /**
     * The length of the slot.
     *
     * @return the length of the slot
     */
    public final int length() {
        return end - start;
    }

    /**
     * The positions that this slot represents.
     *
     * @return the positions that this slot represents.
     */
    public final List<GridCoord> boxPositions() {
        return IntStream.range(start, end)
                .mapToObj(varying -> coordOf(varying, offset))
                .toList();
    }

    /**
     * Evaluates whether the given coordinates belong to this slot.
     *
     * <p>Behaviour should be equivalent to {@code boxPositions().contains(coord)}.
     *
     * @param coord the coordinates
     * @return {@code true} iff the given coordinates belong to this slot
     */
    public final boolean contains(final GridCoord coord) {
        return offsetCoordinateOf(coord) == offset
                && varyingCoordinateOf(coord) >= start
                && varyingCoordinateOf(coord) < end;
    }

    /**
     * Returns the value of the varying coordinate.
     *
     * @param coord some coordinates
     * @return the value of the varying coordinate of the given {@link GridCoord}.
     */
    abstract int varyingCoordinateOf(final GridCoord coord);

    /**
     * Returns the value of the offset coordinate.
     *
     * @param coord some coordinates
     * @return the value of the offset coordinate of the given {@link GridCoord}.
     */
    abstract int offsetCoordinateOf(final GridCoord coord);

    /**
     * Creates a {@link GridCoord}.
     *
     * @param varyingCoordinate the varying coordinate
     * @param offsetCoordinate the offset coordinate
     * @return a new {@link GridCoord}
     */
    abstract GridCoord coordOf(final int varyingCoordinate, final int offsetCoordinate);
}

/** An across (= horizontal) {@link SlotOutline}. */
final class AcrossSlotOutline extends SlotOutline {

    /**
     * Constructs an instance.
     *
     * @param columnStart the slot column start index (inclusive)
     * @param columnEnd the slot column end index (exclusive)
     * @param row the row index
     */
    AcrossSlotOutline(final int columnStart, final int columnEnd, final int row) {
        super(columnStart, columnEnd, row);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final AcrossSlotOutline that)) return false;
        return start == that.start && end == that.end && offset == that.offset;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, offset);
    }

    @Override
    public String toString() {
        return "AcrossSlotOutline{" + "columnStart=" + start + ", columnEnd=" + end + ", row=" + offset + '}';
    }

    @Override
    int varyingCoordinateOf(final GridCoord coord) {
        return coord.column();
    }

    @Override
    int offsetCoordinateOf(final GridCoord coord) {
        return coord.row();
    }

    @Override
    GridCoord coordOf(final int varying, final int offset) {
        return new GridCoord(varying, offset);
    }
}

/** A down (= vertical) {@link SlotOutline}. */
final class DownSlotOutline extends SlotOutline {

    /**
     * Constructs an instance.
     *
     * @param rowStart the slot row start index (inclusive)
     * @param rowEnd the slot row end index (exclusive)
     * @param column the column index
     */
    DownSlotOutline(final int rowStart, final int rowEnd, final int column) {
        super(rowStart, rowEnd, column);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final DownSlotOutline that)) return false;
        return start == that.start && end == that.end && offset == that.offset;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, offset);
    }

    @Override
    public String toString() {
        return "DownSlotOutline{" + "rowStart=" + start + ", rowEnd=" + end + ", column=" + offset + '}';
    }

    @Override
    int varyingCoordinateOf(final GridCoord coord) {
        return coord.row();
    }

    @Override
    int offsetCoordinateOf(final GridCoord coord) {
        return coord.column();
    }

    @Override
    GridCoord coordOf(final int varying, final int offset) {
        return new GridCoord(offset, varying);
    }
}
