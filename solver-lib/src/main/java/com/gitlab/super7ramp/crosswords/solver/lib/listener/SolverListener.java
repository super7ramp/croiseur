package com.gitlab.super7ramp.crosswords.solver.lib.listener;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;

/**
 * Listener on puzzle modification.
 */
public interface SolverListener {

    /**
     * Called when a slot which was entirely filled is cleared.
     *
     * @param slot the cleared slot
     */
    default void onUnassignment(final Slot slot, final String unassignedWord) {
        // Do nothing by default.
    }

    /**
     * Called when a slot is filled.
     *
     * @param slot the assigned slot
     * @param word the value set
     */
    default void onAssignment(final Slot slot, final String word) {
        // Do nothing by default.
    }
}
