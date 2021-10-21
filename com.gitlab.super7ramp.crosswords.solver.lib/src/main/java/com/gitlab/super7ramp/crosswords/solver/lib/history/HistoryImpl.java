package com.gitlab.super7ramp.crosswords.solver.lib.history;

import com.gitlab.super7ramp.crosswords.solver.lib.core.History;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

/**
 * Implementation of {@link History}.
 */
public final class HistoryImpl implements History {

    /** Assigned slots, from old to young. */
    private Deque<Slot> assignedSlots;

    /**
     * Constructor.
     */
    public HistoryImpl() {
        assignedSlots = new ArrayDeque<>();
    }

    @Override
    public void recordAssignment(Slot slot, String value) {
        assignedSlots.add(slot);
    }

    @Override
    public void recordUnassignment(Slot slot) {
        assignedSlots.remove(slot);
    }

    @Override
    public Optional<Slot> lastAssignedSlot() {
        if (assignedSlots.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(assignedSlots.getLast());
    }
}
