package com.gitlab.super7ramp.crosswords.solver.lib.backtrack;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Backtracker;
import com.gitlab.super7ramp.crosswords.solver.lib.core.History;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;

import java.util.Collections;
import java.util.Set;

/**
 * {@link Backtracker} implementation that simply selects the last assigned variable.
 */
final class Backtrack implements Backtracker {

    /** Assignment history. */
    private final History history;

    /**
     * Constructor
     *
     * @param anHistory assignment history
     */
    Backtrack(final History anHistory) {
        history = anHistory;
    }

    @Override
    public Set<Slot> backtrackFrom(final Slot variable) {
        return Collections.singleton(history.lastAssignedSlot().orElseThrow(IllegalStateException::new));
    }
}
