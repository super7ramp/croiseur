package com.gitlab.super7ramp.crosswords.solver.lib.history;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;

import java.util.Iterator;
import java.util.Optional;

/**
 * Consumer: Backtracker.
 */
public interface InstantiationHistoryConsumer {

    /**
     * Get and remove last assigned slot from history.
     *
     * @return the last assigned slot in history if any; otherwise, returns {@link Optional#empty()}
     */
    Optional<Slot> lastAssignedSlot();

    Iterator<Slot> explorer();
}
