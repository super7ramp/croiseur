/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model.slot;

import java.util.List;

/**
 * Updates the slots after a grid dimension change.
 */
abstract sealed class DimensionChangeProcessor {

    /** The slots collinear to the added/removed slot(s). */
    private final List<SlotOutline> collinearSlots;

    /** The slots orthogonal to the changed side to the added/removed slot(s). */
    private final List<SlotOutline> orthogonalSlots;

    /**
     * Constructs an instance.
     *
     * @param collinearSlotsArg  the slots collinear to the added/removed slot(s)
     * @param orthogonalSlotsArg the slots orthogonal to the added/removed slot(s)
     */
    DimensionChangeProcessor(final List<SlotOutline> collinearSlotsArg, final List<SlotOutline> orthogonalSlotsArg) {
        collinearSlots = collinearSlotsArg;
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
            collinearSlots.clear();
            orthogonalSlots.clear();
            return;
        }

        // Update collinear slots
        if (oldLength > newLength) {
            collinearSlots.removeIf(slot -> slot.offset >= newLength);
        } else {
            for (int row = oldLength; row < newLength; row++) {
                collinearSlots.add(collinearSlotOf(0, otherDimensionLength, row));
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
            if (slot.start < newLength) {
                it.set(orthogonalSlotOf(slot.start, newLength, slot.offset));
            } else {
                it.remove();
            }
        }
    }

    /**
     * Creates a new slot, collinear to the added/removed slot(s).
     *
     * @param start  the start index (inclusive)
     * @param end    the end index (exclusive)
     * @param offset the offset
     * @return a new slot
     */
    abstract SlotOutline collinearSlotOf(final int start, final int end, final int offset);

    /**
     * Creates a new slot, orthogonal to the added/removed slot(s).
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
    ColumnCountChangeProcessor(final List<SlotOutline> downSlots, final List<SlotOutline> acrossSlots) {
        super(downSlots, acrossSlots);
    }

    @Override
    SlotOutline collinearSlotOf(final int start, final int end, final int offset) {
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
    RowCountChangeProcessor(final List<SlotOutline> downSlots, final List<SlotOutline> acrossSlots) {
        super(acrossSlots, downSlots);
    }

    @Override
    SlotOutline collinearSlotOf(final int start, final int end, final int offset) {
        return SlotOutline.across(start, end, offset);
    }

    @Override
    SlotOutline orthogonalSlotOf(final int start, final int end, final int offset) {
        return SlotOutline.down(start, end, offset);
    }
}
