package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import com.gitlab.super7ramp.crosswords.solver.api.Coordinate;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Assignment;
import com.gitlab.super7ramp.crosswords.solver.lib.core.ProbablePuzzle;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of {@link Grid}.
 */
final class GridImpl implements Grid {

    /**
     * Implementation of {@link ProbablePuzzle}.
     */
    private static class PuzzleImpl implements ProbablePuzzle {

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
        public Set<Slot> slots() {
            return data.slots().entrySet().stream()
                    .map(entry -> new SlotImpl(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toSet());
        }

        @Override
        public Set<Slot> connectedSlots(final Slot slot) {
            return data.connectedSlots(slot.uid()).entrySet().stream()
                    .map(entry -> new SlotImpl(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toSet());
        }

        @Override
        public ProbablePuzzle probe(final Assignment assignment) {
            final GridData probedData = data.copy();
            probedData.slot(assignment.slotUid()).write(assignment.word());
            return new PuzzleImpl(probedData);
        }
    }

    /**
     * The underlying data.
     */
    private final GridData data;

    /**
     * The {@link ProbablePuzzle} implementation.
     */
    private ProbablePuzzle puzzle;

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
    public ProbablePuzzle puzzle() {
        return puzzle;
    }

    @Override
    public Map<Coordinate, Character> boxes() {
        return data.toBoxes();
    }
}
