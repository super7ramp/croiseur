package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import com.gitlab.super7ramp.crosswords.solver.api.GridPosition;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.lookahead.Assignment;
import com.gitlab.super7ramp.crosswords.solver.lib.lookahead.Unassignment;

import java.util.Collection;
import java.util.Map;

import static java.util.stream.Collectors.toUnmodifiableSet;

/**
 * Implementation of {@link Grid}.
 */
final class GridImpl implements Grid {

    /**
     * Implementation of {@link Puzzle}.
     */
    private static class PuzzleImpl implements Puzzle {

        /**
         * The underlying data.
         */
        private final GridData data;

        /**
         * Constructor.
         *
         * @param someData actual data model
         */
        PuzzleImpl(final GridData someData) {
            data = someData;
        }

        @Override
        public Collection<Slot> slots() {
            return data.slots()
                       .entrySet()
                       .stream()
                       .map(entry -> new SlotImpl(entry.getKey(), entry.getValue(),
                               data.connectedSlots(entry.getKey())))
                       .collect(toUnmodifiableSet());
        }

        @Override
        public Collection<Slot> probe(final Assignment assignment) {
            final GridData probedData = data.copy();
            probedData.slot(assignment.slotUid()).write(assignment.word());
            return new PuzzleImpl(probedData).slots();
        }

        @Override
        public Collection<Slot> probe(final Unassignment unassignment) {
            final GridData probedData = data.copy();
            probedData.slot(unassignment.slotUid()).clear();
            return new PuzzleImpl(probedData).slots();
        }
    }

    /**
     * The {@link Puzzle} implementation.
     */
    private final Puzzle puzzle;

    /**
     * The underlying data.
     */
    private final GridData data;

    /**
     * Constructor.
     *
     * @param someData actual data model
     */
    GridImpl(final GridData someData) {
        data = someData;
        puzzle = new PuzzleImpl(someData);
    }

    @Override
    public Puzzle puzzle() {
        return puzzle;
    }

    @Override
    public Map<GridPosition, Character> boxes() {
        return data.toBoxes();
    }
}
