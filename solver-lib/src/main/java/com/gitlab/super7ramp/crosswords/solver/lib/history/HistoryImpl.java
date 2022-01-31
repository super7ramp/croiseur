package com.gitlab.super7ramp.crosswords.solver.lib.history;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Optional;

/**
 * Implementation of {@link History}.
 */
final class HistoryImpl implements HistoryWriter {

    /** Assigned slots, from old to young. */
    private final Deque<Slot> assignedSlots;

    /**
     * Constructor.
     */
    HistoryImpl() {
        assignedSlots = new ArrayDeque<>();
    }

    @Override
    public Optional<Slot> lastAssignedSlot() {
        return Optional.ofNullable(assignedSlots.peekLast());
    }

    @Override
    public Optional<Slot> lastAssignedConnectedSlot(Slot toBeConnectedWith) {
        final Iterator<Slot> it = assignedSlots.descendingIterator();
        while (it.hasNext()) {
            final Slot s = it.next();
            if (s.isConnectedTo(toBeConnectedWith)) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }

    @Override
    public Iterator<Slot> explorer() {
        // FIXME should be read-only
        return assignedSlots.descendingIterator();
    }

    @Override
    public void addAssignmentRecord(final Slot slot) {
        assignedSlots.add(slot);
    }

    @Override
    public void removeAssignmentRecord(Slot slot) {
        assignedSlots.remove(slot);
    }
}
