/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model.slot;

import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordBoxViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.GridCoord;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Updates a list of slots after a box has been lightened, with the minimal number of
 * modifications.
 * <p>
 * Given slots must be of same type: Either across or down.
 */
abstract class LightenedBoxProcessor {

    /** The slots to update. */
    private final List<SlotOutline> slots;

    /** The boxes. */
    private final Map<GridCoord, CrosswordBoxViewModel> boxes;

    /** The max index of a box. */
    private final Supplier<Integer> maxIndex;

    /**
     * Constructs an instance.
     *
     * @param slotsArg    the slots to update
     * @param boxesArg    the boxes (read-only)
     * @param maxIndexArg the max index of a box
     */
    protected LightenedBoxProcessor(final List<SlotOutline> slotsArg,
                                    final Map<GridCoord, CrosswordBoxViewModel> boxesArg,
                                    final Supplier<Integer> maxIndexArg) {
        slots = slotsArg;
        boxes = boxesArg;
        maxIndex = maxIndexArg;
    }

    /**
     * Updates the slots after the enlightenment of the box at given coordinates.
     *
     * @param lightenedBoxCoordinates the coordinates of the lightened box
     */
    final void updateSlotsAfterEnlightenmentOf(final GridCoord lightenedBoxCoordinates) {
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
            } else {
                // Grow first half to glob lightened box and maybe some previously ignored boxes
                int end = lightenedBoxIndex + 1;
                for (int i = end + 1; i <= maxIndex.get(); i++) {
                    if (i != maxIndex.get() &&
                        boxes.get(coordOf(i, lightenedBoxOffset)).isShaded()) {
                        break;
                    }
                    end = i;
                }
                updatedFirstHalf = slotOf(firstHalf.start, end, lightenedBoxOffset);
            }
            final int firstHalfIndex = slots.indexOf(firstHalf);
            slots.set(firstHalfIndex, updatedFirstHalf);
        } else if (secondHalfOpt.isPresent()) {
            // Grow second half to glob lightened box and maybe some previously ignored boxes
            int start = lightenedBoxIndex;
            for (int i = start - 1; i >= 0; i--) {
                if (boxes.get(coordOf(i, lightenedBoxOffset)).isShaded()) {
                    break;
                }
                start = i;
            }
            final SlotOutline secondHalf = secondHalfOpt.get();
            final SlotOutline updatedSecondHalf = slotOf(start, secondHalf.end, lightenedBoxOffset);
            final int secondHalfIndex = slots.indexOf(secondHalf);
            slots.set(secondHalfIndex, updatedSecondHalf);
        } else {
            // TODO create new slots from previously ignored box groups because not long enough
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

    /**
     * Creates a {@link GridCoord}.
     *
     * @param varying a box index
     * @param offset  a box offset
     * @return the created {@link GridCoord}
     */
    abstract GridCoord coordOf(final int varying, final int offset);
}

/**
 * {@link LightenedBoxProcessor} for across slots.
 */
final class AcrossSlotsLightenedBoxProcessor extends LightenedBoxProcessor {

    /**
     * Constructs an instance.
     *
     * @param acrossSlots the slots to update
     * @param boxes       the boxes (read-only)
     * @param columnCount the column count
     */
    AcrossSlotsLightenedBoxProcessor(final List<SlotOutline> acrossSlots,
                                     final Map<GridCoord, CrosswordBoxViewModel> boxes,
                                     final Supplier<Integer> columnCount) {
        super(acrossSlots, boxes, columnCount);
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

    @Override
    GridCoord coordOf(final int varying, final int offset) {
        return new GridCoord(varying, offset);
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
     * @param boxes     the boxes (read-only)
     * @param rowCount  the row count
     */
    DownSlotsLightenedBoxProcessor(final List<SlotOutline> downSlots,
                                   final Map<GridCoord, CrosswordBoxViewModel> boxes,
                                   final Supplier<Integer> rowCount) {
        super(downSlots, boxes, rowCount);
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

    @Override
    GridCoord coordOf(final int varying, final int offset) {
        return new GridCoord(offset, varying);
    }
}