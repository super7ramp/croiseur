package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Assignment;
import com.gitlab.super7ramp.crosswords.solver.lib.core.ProbablePuzzle;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Puzzle;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of {@link Puzzle}.
 */
final class PuzzleImpl implements ProbablePuzzle {

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
    public Set<Slot> connectedSlots(Slot slot) {
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
