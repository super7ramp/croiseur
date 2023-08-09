/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model.slot;

import com.gitlab.super7ramp.croiseur.gui.view.model.GridCoord;

import java.util.List;

/**
 * Updates a list of slots after a box has been shaded, with the minimal number of modifications.
 * <p>
 * Given slots must be of same type: Either across or down.
 */
abstract sealed class ShadedBoxProcessor {

    /** The list of slots to update. */
    private final List<SlotOutline> slots;

    /**
     * Constructs an instance.
     *
     * @param slotsArg the slots to update
     */
    ShadedBoxProcessor(final List<SlotOutline> slotsArg) {
        slots = slotsArg;
    }

    /**
     * Updates the slots after the shading of the box at given coordinates.
     *
     * @param shadedBoxCoordinates the coordinates of the shaded box
     */
    final void process(final GridCoord shadedBoxCoordinates) {
        final SlotOutline slot =
                slots.stream().filter(s -> s.contains(shadedBoxCoordinates)).findFirst()
                     .orElseThrow();
        final int slotIndex = slots.indexOf(slot);
        final int shadedBoxIndex = varyingCoordinateOf(shadedBoxCoordinates);

        // Put first half at existing slot position, if it is long enough.
        if (shadedBoxIndex - slot.start > 0) {
            final SlotOutline firstHalf = slotOf(slot.start, shadedBoxIndex, slot.offset);
            slots.set(slotIndex, firstHalf);

            // Create a new slot for second half, if it is long enough
            if (slot.end - (shadedBoxIndex + 1) > 0) {
                final SlotOutline secondHalf = slotOf(shadedBoxIndex + 1, slot.end, slot.offset);
                slots.add(slotIndex + 1, secondHalf);
            }
        } else {
            // First half is too small, drop it. Put second half at existing slot position, if it
            // is long enough.
            if (slot.end - (shadedBoxIndex + 1) > 0) {
                final SlotOutline secondHalf = slotOf(shadedBoxIndex + 1, slot.end, slot.offset);
                slots.set(slotIndex, secondHalf);
            } else {
                // Neither first half nor second half are long enough: Drop the slot.
                slots.remove(slotIndex);
            }
        }
    }

    /**
     * Creates a new slot.
     *
     * @param start  the start index (inclusive)
     * @param end    the end index (exclusive)
     * @param offset the offset
     * @return a new slot
     */
    abstract SlotOutline slotOf(final int start, final int end, final int offset);

    /**
     * Returns the value of the varying coordinate - from the point of the view of the slot type -
     * of the given {@link GridCoord}.
     *
     * @param coord some coordinates
     * @return the value of the varying coordinate of the given {@link GridCoord}.
     */
    abstract int varyingCoordinateOf(final GridCoord coord);
}

/**
 * {@link ShadedBoxProcessor} for across slots.
 */
final class AcrossSlotShadedBoxProcessor extends ShadedBoxProcessor {

    /**
     * Constructs an instance.
     *
     * @param acrossSlots the list of slots to update
     */
    AcrossSlotShadedBoxProcessor(final List<SlotOutline> acrossSlots) {
        super(acrossSlots);
    }

    @Override
    SlotOutline slotOf(final int start, final int end, final int offset) {
        return SlotOutline.across(start, end, offset);
    }

    @Override
    int varyingCoordinateOf(final GridCoord coord) {
        return coord.column();
    }
}

/**
 * {@link ShadedBoxProcessor} for down slots.
 */
final class DownSlotShadedBoxProcessor extends ShadedBoxProcessor {

    /**
     * Constructs an instance.
     *
     * @param downSlots the list of slots to update
     */
    DownSlotShadedBoxProcessor(final List<SlotOutline> downSlots) {
        super(downSlots);
    }

    @Override
    SlotOutline slotOf(final int start, final int end, final int offset) {
        return SlotOutline.down(start, end, offset);
    }

    @Override
    int varyingCoordinateOf(final GridCoord coord) {
        return coord.row();
    }
}
