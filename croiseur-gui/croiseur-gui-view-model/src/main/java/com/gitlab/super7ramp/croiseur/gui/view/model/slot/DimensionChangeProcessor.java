/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model.slot;

import java.util.List;

/**
 * Updates the slots after a grid dimension change.
 */
abstract sealed class DimensionChangeProcessor {

    /** The slots parallel to the changed side. */
    private final List<SlotOutline> parallelSlots;

    /** The slots orthogonal to the changed side. */
    private final List<SlotOutline> orthogonalSlots;

    /**
     * Constructs an instance.
     *
     * @param parallelSlotsArg   the slots parallel to the changed side
     * @param orthogonalSlotsArg the slots orthogonal to the changed side
     */
    DimensionChangeProcessor(final List<SlotOutline> parallelSlotsArg,
                             final List<SlotOutline> orthogonalSlotsArg) {
        parallelSlots = parallelSlotsArg;
        orthogonalSlots = orthogonalSlotsArg;
    }

    /**
     * Processes the dimension change
     *
     * @param oldLength            the old dimension value
     * @param newLength            the new dimension valye
     * @param otherDimensionLength the other dimension value
     */
    final void process(final int oldLength, final int newLength, final int otherDimensionLength) {
        if (newLength == 0) {
            parallelSlots.clear();
            orthogonalSlots.clear();
            return;
        }

        // Update parallel slots
        if (oldLength > newLength) {
            parallelSlots.subList(newLength, oldLength).clear();
        } else {
            for (int row = oldLength; row < newLength; row++) {
                parallelSlots.add(parallelSlotOf(0, otherDimensionLength, row));
            }
        }

        // Reevaluate orthogonal slots
        final var it = orthogonalSlots.listIterator();
        while (it.hasNext()) {
            final SlotOutline slot = it.next();
            if (slot.end < oldLength && slot.end < newLength) {
                // Not impacted
                continue;
            }
            if (newLength - slot.start > 0) {
                it.set(orthogonalSlotOf(slot.start, newLength, slot.offset));
            } else {
                it.remove();
            }
        }
    }

    /**
     * Creates a new slot, parallel to the changed side.
     *
     * @param start  the start index (inclusive)
     * @param end    the end index (exclusive)
     * @param offset the offset
     * @return a new slot
     */
    abstract SlotOutline parallelSlotOf(final int start, final int end, final int offset);

    /**
     * Creates a new slot, orthogonal to the changed side.
     *
     * @param start  the start index (inclusive)
     * @param end    the end index (exclusive)
     * @param offset the offset
     * @return a new slot
     */
    abstract SlotOutline orthogonalSlotOf(final int start, final int end, final int offset);
}

/**
 * {@link DimensionChangeProcessor} for grid width.
 */
final class ColumnCountChangeProcessor extends DimensionChangeProcessor {

    /**
     * Constructs an instance.
     *
     * @param downSlots   the down slots
     * @param acrossSlots the across slots
     */
    ColumnCountChangeProcessor(final List<SlotOutline> downSlots,
                               final List<SlotOutline> acrossSlots) {
        super(downSlots, acrossSlots);
    }

    @Override
    SlotOutline parallelSlotOf(final int start, final int end, final int offset) {
        return SlotOutline.down(start, end, offset);
    }

    @Override
    SlotOutline orthogonalSlotOf(final int start, final int end, final int offset) {
        return SlotOutline.across(start, end, offset);
    }
}

/**
 * {@link DimensionChangeProcessor} for grid height.
 */
final class RowCountChangeProcessor extends DimensionChangeProcessor {

    /**
     * Constructs an instance.
     *
     * @param downSlots   the down slots
     * @param acrossSlots the across slots
     */
    RowCountChangeProcessor(final List<SlotOutline> downSlots,
                            final List<SlotOutline> acrossSlots) {
        super(acrossSlots, downSlots);
    }

    @Override
    SlotOutline parallelSlotOf(final int start, final int end, final int offset) {
        return SlotOutline.across(start, end, offset);
    }

    @Override
    SlotOutline orthogonalSlotOf(final int start, final int end, final int offset) {
        return SlotOutline.down(start, end, offset);
    }
}