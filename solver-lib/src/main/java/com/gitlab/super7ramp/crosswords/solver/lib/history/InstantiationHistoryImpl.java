package com.gitlab.super7ramp.crosswords.solver.lib.history;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Optional;

/**
 * Implementation of {@link InstantiationHistory}.
 */
final class InstantiationHistoryImpl implements InstantiationHistory {

    /** Assigned slots, from old to young. */
    private Deque<Slot> assignedSlots;

    /**
     * Constructor.
     */
    InstantiationHistoryImpl() {
        assignedSlots = new ArrayDeque<>();
    }

    @Override
    public Optional<Slot> lastAssignedSlot() {
        return Optional.ofNullable(assignedSlots.pollLast());
    }

    @Override
    public Iterator<Slot> explorer() {
        return assignedSlots.descendingIterator();
    }

    @Override
    public void recordAssignment(final Slot slot, final String value) {
        assignedSlots.add(slot);
    }
}
