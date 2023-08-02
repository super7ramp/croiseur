/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model.slot;

import com.gitlab.super7ramp.croiseur.gui.view.model.GridCoord;

import java.util.List;

/**
 * Slot outline base class.
 */
public abstract sealed class SlotOutline permits AcrossSlotOutline, DownSlotOutline {

    /** The varying coordinate start index. */
    protected final int start;

    /** The varying coordinate end index. */
    protected final int end;

    /** The other coordinate fixed value. */
    protected final int offset;

    /**
     * Constructs an instance.
     *
     * @param startArg  the start index
     * @param endArg    the end index
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
     * @param columnEnd   the slot column end index (exclusive)
     * @param row         the row index
     * @return a slot across the given column start and end indexes, at given row index
     */
    public static SlotOutline across(final int columnStart, final int columnEnd, final int row) {
        return new AcrossSlotOutline(columnStart, columnEnd, row);
    }

    /**
     * Creates a slot down the given row start and end indexes, at given column index.
     *
     * @param rowStart the slot row start index (inclusive)
     * @param rowEnd   the slot row end index (exclusive)
     * @param column   the column index
     * @return a slot down the given row start and end indexes, at given column index
     */
    public static SlotOutline down(final int rowStart, final int rowEnd, final int column) {
        return new DownSlotOutline(rowStart, rowEnd, column);
    }

    /**
     * The positions that this slot represents.
     *
     * @return the positions that this slot represents.
     */
    public abstract List<GridCoord> boxPositions();

    /**
     * Evaluates whether the given coordinates belong to this slot.
     *
     * @param coord the coordinates
     * @return {@code true} iff the given coordinates belong to this slot
     */
    public abstract boolean contains(final GridCoord coord);
}
