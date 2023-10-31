/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model.slot;

import re.belv.croiseur.gui.view.model.GridCoord;

import java.util.List;
import java.util.Optional;

/**
 * Updates a list of slots after a box has been lightened, with the minimal number of
 * modifications.
 * <p>
 * Given slots must be of same type: Either across or down.
 */
abstract sealed class LightenedBoxProcessor {

    /** The slots to update. */
    private final List<SlotOutline> slots;

    /**
     * Constructs an instance.
     *
     * @param slotsArg the slots to update
     */
    protected LightenedBoxProcessor(final List<SlotOutline> slotsArg) {
        slots = slotsArg;
    }

    /**
     * Updates the slots after the enlightenment of the box at given coordinates.
     *
     * @param lightenedBoxCoordinates the coordinates of the lightened box
     */
    final void process(final GridCoord lightenedBoxCoordinates) {
        final int lightenedBoxOffset = offsetCoordinateOf(lightenedBoxCoordinates);
        final int lightenedBoxIndex = varyingCoordinateOf(lightenedBoxCoordinates);

        final Optional<SlotOutline> firstHalfOpt =
                slots.stream().filter(slot -> slot.offset == lightenedBoxOffset &&
                                              slot.end == lightenedBoxIndex)
                     .findFirst();
        final Optional<SlotOutline> secondHalfOpt =
                slots.stream().filter(slot -> slot.offset == lightenedBoxOffset &&
                                              slot.start == lightenedBoxIndex + 1)
                     .findFirst();

        if (firstHalfOpt.isPresent()) {
            final SlotOutline firstHalf = firstHalfOpt.get();
            final SlotOutline updatedFirstHalf;
            if (secondHalfOpt.isPresent()) {
                // Merge both halves in first half slot
                final SlotOutline secondHalf = secondHalfOpt.get();
                updatedFirstHalf = slotOf(firstHalf.start, secondHalf.end, lightenedBoxOffset);
                slots.remove(secondHalf);
            } else {
                updatedFirstHalf =
                        slotOf(firstHalf.start, lightenedBoxIndex + 1, lightenedBoxOffset);
            }
            final int firstHalfIndex = slots.indexOf(firstHalf);
            slots.set(firstHalfIndex, updatedFirstHalf);
        } else if (secondHalfOpt.isPresent()) {
            final SlotOutline secondHalf = secondHalfOpt.get();
            final SlotOutline updatedSecondHalf =
                    slotOf(lightenedBoxIndex, secondHalf.end, lightenedBoxOffset);
            final int secondHalfIndex = slots.indexOf(secondHalf);
            slots.set(secondHalfIndex, updatedSecondHalf);
        } else {
            // Box is between 2 shaded boxes/borders
            final SlotOutline newSlot =
                    slotOf(lightenedBoxIndex, lightenedBoxIndex + 1, lightenedBoxOffset);
            final int insertionIndex = insertionIndexFor(newSlot);
            slots.add(insertionIndex, newSlot);
        }
    }

    /**
     * Finds the insertion index for the given new slot.
     *
     * @param newSlot the new slot
     * @return the insertion index
     */
    private int insertionIndexFor(final SlotOutline newSlot) {
        int insertionIndex = 0;
        final var it = slots.listIterator();
        while (it.hasNext()) {
            final var slot = it.next();
            if (slot.offset < newSlot.offset ||
                slot.offset == newSlot.offset && slot.end < newSlot.start) {
                insertionIndex = it.nextIndex();
            } else {
                break;
            }
        }
        return insertionIndex;
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
     * Returns the value of the offset coordinate - from the point of the view of the slot type - of
     * the given {@link GridCoord}.
     *
     * @param coord some coordinates
     * @return the value of the varying coordinate of the given {@link GridCoord}.
     */
    abstract int offsetCoordinateOf(final GridCoord coord);

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
 * {@link LightenedBoxProcessor} for across slots.
 */
final class AcrossSlotsLightenedBoxProcessor extends LightenedBoxProcessor {

    /**
     * Constructs an instance.
     *
     * @param acrossSlots the slots to update
     */
    AcrossSlotsLightenedBoxProcessor(final List<SlotOutline> acrossSlots) {
        super(acrossSlots);
    }

    @Override
    SlotOutline slotOf(final int start, final int end, final int offset) {
        return SlotOutline.across(start, end, offset);
    }

    @Override
    int offsetCoordinateOf(final GridCoord coord) {
        return coord.row();
    }

    @Override
    int varyingCoordinateOf(final GridCoord coord) {
        return coord.column();
    }
}

/**
 * {@link LightenedBoxProcessor} for down slots.
 */
final class DownSlotsLightenedBoxProcessor extends LightenedBoxProcessor {

    /**
     * Constructs an instance.
     *
     * @param downSlots the slots to update
     */
    DownSlotsLightenedBoxProcessor(final List<SlotOutline> downSlots) {
        super(downSlots);
    }

    @Override
    SlotOutline slotOf(final int start, final int end, final int offset) {
        return SlotOutline.down(start, end, offset);
    }

    @Override
    int offsetCoordinateOf(final GridCoord coord) {
        return coord.column();
    }

    @Override
    int varyingCoordinateOf(final GridCoord coord) {
        return coord.row();
    }
}