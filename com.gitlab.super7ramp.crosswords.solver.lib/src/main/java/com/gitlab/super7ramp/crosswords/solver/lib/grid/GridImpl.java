package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import com.gitlab.super7ramp.crosswords.solver.api.Coordinate;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.lookahead.Assignment;
import com.gitlab.super7ramp.crosswords.solver.lib.lookahead.Unassignment;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of {@link Grid}.
 */
final class GridImpl implements Grid {

    /**
     * Implementation of {@link Puzzle}.
     */
    private static class PuzzleImpl implements Puzzle {

        /** The underlying data. */
        private final GridData data;

        /** The slots. */
        private final Set<Slot> slots;


        /**
         * Constructor.
         *
         * @param someData actual data model
         */
        PuzzleImpl(final GridData someData) {
            data = someData;
            slots = data.slots().entrySet().stream()
                    .map(entry -> new SlotImpl(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toSet());
        }

        @Override
        public Set<Slot> slots() {
            return Collections.unmodifiableSet(slots);
        }

        @Override
        public Set<Slot> connectedSlots(final Slot slot) {
            return data.connectedSlots(slot.uid()).entrySet().stream()
                    .map(entry -> new SlotImpl(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toSet());
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

    /** The underlying data. */
    private final GridData data;

    /** The {@link Puzzle} implementation. */
    private Puzzle puzzle;

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
    public Map<Coordinate, Character> boxes() {
        return data.toBoxes();
    }
}
