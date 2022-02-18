package com.gitlab.super7ramp.crosswords.solver.ginsberg.history;

import com.gitlab.super7ramp.crosswords.solver.ginsberg.core.Slot;

import java.util.Iterator;
import java.util.Optional;

/**
 * Assignment history. Useful for backtracking.
 */
public interface History {

    /**
     * Get the last assigned slot from history.
     *
     * @return the last assigned slot in history if any; otherwise, returns {@link Optional#empty()}
     */
    Optional<Slot> lastAssignedSlot();

    /**
     * Get the last assigned slot from history, connected to given variable.
     *
     * @return the last assigned slot connected to given slot, if any; otherwise, returns
     * {@link Optional#empty()}
     */
    Optional<Slot> lastAssignedConnectedSlot(final Slot toBeConnectedWith);

    /**
     * Get an iterator on the history.
     * <p>
     * Element removal is not supported - this class is for read-only access. Behaviour on element
     * removal is not fully determined yet, at best it will be no effect, otherwise it will throw a
     * runtime exception.
     *
     * @return the iterator on history
     */
    Iterator<Slot> explorer();
}
