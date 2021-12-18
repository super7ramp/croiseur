package com.gitlab.super7ramp.crosswords.solver.lib.core;

/**
 * Listener on puzzle modification.
 */
public interface SolverUpdateListener {

    /**
     * Called when a slot which was <strong>not</strong> entirely filled is cleared.
     *
     * @param slot the cleared slot
     */
    void onPartialUnassignment(final Slot slot);

    /**
     * Called when a slot which was entirely filled is cleared.
     *
     * @param slot the cleared slot
     */
    void onUnassignment(final Slot slot, final String unassignedWord);

    /**
     * Called when a slot is filled.
     *
     * @param slot the assigned slot
     * @param word the value set
     */
    void onAssignment(final Slot slot, final String word);
}
