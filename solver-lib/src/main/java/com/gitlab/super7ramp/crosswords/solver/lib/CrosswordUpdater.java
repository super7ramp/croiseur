package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.core.SlotIdentifier;
import com.gitlab.super7ramp.crosswords.solver.lib.core.sap.Elimination;
import com.gitlab.super7ramp.crosswords.solver.lib.core.sap.ProblemStateUpdater;
import com.gitlab.super7ramp.crosswords.solver.lib.listener.SolverListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Updates crossword problem state.
 * <p>
 * The update is performed in this order:
 * <ol>
 *     <li>Variables</li>
 *     <li>Eliminated values: Depends on variable states after unassignment</li>
 *     <li>Candidates: Depends on both variable states and eliminated values</li>
 * </ol>
 * The rest can be done in any order.
 */
public class CrosswordUpdater implements ProblemStateUpdater<Slot, String, SlotIdentifier> {

    /** The crossword state. */
    private final Crossword crossword;

    /** Listeners. */
    private final List<SolverListener> listeners;

    /**
     * Constructor.
     *
     * @param aCrossword the crossword state
     */
    CrosswordUpdater(final Crossword aCrossword) {
        crossword = Objects.requireNonNull(aCrossword);
        listeners = new ArrayList<>();
    }

    CrosswordUpdater withListeners(SolverListener... someListeners) {
        listeners.addAll(Arrays.asList(someListeners));
        return this;
    }

    @Override
    public void assign(final Slot variable, final String value) {
        variable.assign(value);
        crossword.history().addAssignmentRecord(variable);
        crossword.dictionary().updateCache(variable);
        listeners.forEach(listener -> listener.onAssignment(variable, value));
    }

    @Override
    public void unassign(final Elimination<Slot, SlotIdentifier> elimination) {
        final Slot variable = elimination.eliminated();
        final String oldValue = variable.unassign();
        crossword.history().removeAssignmentRecord(variable);
        crossword.eliminationSpace().eliminate(variable.uid(), elimination.reason(), oldValue);
        crossword.dictionary().invalidateCache(variable);
        listeners.forEach(listener -> listener.onUnassignment(variable, oldValue));
    }
}
