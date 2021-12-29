package com.gitlab.super7ramp.crosswords.solver.lib.history;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.core.SolverListener;

/**
 * Consumer: instantiation.
 */
public interface InstantiationHistoryProducer extends SolverListener {

    @Override
    default void onUnassignment(final Slot slot, final String unassignedWord) {
        // Do nothing.
    }

    @Override
    default void onPartialUnassignment(final Slot slot) {
        // Do nothing.
    }

    @Override
    default void onAssignment(final Slot slot, final String word) {
        recordAssignment(slot, word);
    }

    void recordAssignment(Slot slot, String value);

}
