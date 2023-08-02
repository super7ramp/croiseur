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
 * An across (= horizontal) {@link SlotOutline}.
 */
final class AcrossSlotOutline extends SlotOutline {

    /**
     * Constructs an instance.
     *
     * @param columnStart the slot column start index (inclusive)
     * @param columnEnd   the slot column end index (exclusive)
     * @param row         the row index
     */
    AcrossSlotOutline(final int columnStart, final int columnEnd, final int row) {
        super(columnStart, columnEnd, row);
    }

    @Override
    public List<GridCoord> boxPositions() {
        return IntStream.range(start, end).mapToObj(column -> new GridCoord(column, offset))
                        .toList();
    }

    @Override
    public boolean contains(final GridCoord coord) {
        return coord.row() == offset && coord.column() >= start && coord.column() < end;
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
        return "AcrossSlotOutline{" +
               "columnStart=" + start +
               ", columnEnd=" + end +
               ", row=" + offset +
               '}';
    }
}
