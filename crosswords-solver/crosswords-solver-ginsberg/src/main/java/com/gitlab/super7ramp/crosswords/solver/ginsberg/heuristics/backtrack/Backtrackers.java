package com.gitlab.super7ramp.crosswords.solver.ginsberg.heuristics.backtrack;

import com.gitlab.super7ramp.crosswords.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.core.SlotIdentifier;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.core.sap.Backtracker;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.core.sap.Elimination;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.history.History;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A factory of backtracking strategies.
 */
public final class Backtrackers {

    /**
     * Private constructor, static factory methods only.
     */
    Backtrackers() {
        // Nothing to do.
    }

    /**
     * Returns the better {@link Backtracker} in most situations.
     *
     * @param history assignment history
     * @return the better {@link Backtracker} in most situations.
     */
    public static Backtracker<Slot, SlotIdentifier> defaultBacktrack(final History history) {
        return dynamicBacktrack(history);
    }

    /**
     * A {@link Backtracker} implementation that simply selects the last assigned variable.
     *
     * @param history assignment history
     * @return a {@link Backtracker} implementation that simply selects the last assigned variable
     */
    public static Backtracker<Slot, SlotIdentifier> backtrack(final History history) {
        return slot -> history.lastAssignedSlot()
                              .map(toUnassign -> eliminationOf(toUnassign, slot))
                              .map(Collections::singletonList)
                              .orElse(Collections.emptyList());
    }

    /**
     * A {@link Backtracker} implementation that selects the last assigned variable connected to
     * the unassignable variable.
     *
     * @param history assignment history
     * @return the dynamic backtrack strategy
     */
    public static Backtracker<Slot, SlotIdentifier> dynamicBacktrack(final History history) {
        return slot -> history.lastAssignedConnectedSlot(slot)
                              .map(toUnassign -> eliminationOf(toUnassign, slot))
                              .map(Collections::singletonList)
                              .orElse(Collections.emptyList());
    }

    /**
     * A {@link Backtracker} implementation that selects the last assigned variable connected to
     * the unassignable variable and any variables assigned after it.
     * <p>
     * Same as {@link #dynamicBacktrack(History)} but force removal of maybe rightly assigned
     * variables.
     *
     * @param history assignment history
     * @return the backjump {@link Backtracker}
     */
    public static Backtracker<Slot, SlotIdentifier> backjump(final History history) {
        return slot -> {
            final Iterator<Slot> it = history.explorer();
            final List<Elimination<Slot, SlotIdentifier>> toUnassign = new ArrayList<>();
            while (it.hasNext()) {
                final Slot next = it.next();
                toUnassign.add(eliminationOf(next, slot));
                if (slot.isConnectedTo(next)) {
                    break;
                }
            }
            return toUnassign;
        };
    }

    private static Elimination<Slot, SlotIdentifier> eliminationOf(final Slot toUnassign,
                                                                   final Slot unassignable) {
        return new Elimination<>(toUnassign, unassignable.connectedSlots());
    }
}
