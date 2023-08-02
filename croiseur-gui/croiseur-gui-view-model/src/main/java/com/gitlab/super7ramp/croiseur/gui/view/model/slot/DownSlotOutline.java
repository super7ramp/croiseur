/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model.slot;

import com.gitlab.super7ramp.croiseur.gui.view.model.GridCoord;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * A down (= vertical) {@link SlotOutline}.
 */
final class DownSlotOutline extends SlotOutline {

    /**
     * Constructs an instance.
     *
     * @param rowStart the slot row start index (inclusive)
     * @param rowEnd   the slot row end index (exclusive)
     * @param column   the column index
     */
    DownSlotOutline(final int rowStart, final int rowEnd, final int column) {
        super(rowStart, rowEnd, column);
    }

    @Override
    public List<GridCoord> boxPositions() {
        return IntStream.range(start, end).mapToObj(row -> new GridCoord(offset, row)).toList();
    }

    @Override
    public boolean contains(final GridCoord coord) {
        return coord.column() == offset && coord.row() >= start && coord.row() < end;
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
        return "DownSlotOutline{" +
               "rowStart=" + start +
               ", rowEnd=" + end +
               ", column=" + offset +
               '}';
    }
}
