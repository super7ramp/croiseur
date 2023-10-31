/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.state;

import re.belv.croiseur.solver.ginsberg.core.Slot;
import re.belv.croiseur.solver.ginsberg.core.SlotIdentifier;
import re.belv.croiseur.solver.ginsberg.core.sap.Elimination;
import re.belv.croiseur.solver.ginsberg.core.sap.ProblemStateUpdater;
import re.belv.croiseur.solver.ginsberg.listener.SolverListener;

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
public final class CrosswordUpdater implements ProblemStateUpdater<Slot, String, SlotIdentifier> {

    /** The crossword state. */
    private final Crossword crossword;

    /** Listeners. */
    private final List<SolverListener> listeners;

    /**
     * Constructor.
     *
     * @param aCrossword the crossword state
     */
    public CrosswordUpdater(final Crossword aCrossword) {
        crossword = Objects.requireNonNull(aCrossword);
        listeners = new ArrayList<>();
    }

    /**
     * Adds some listeners.
     *
     * @param someListeners the listeners to add
     * @return this updater
     */
    public CrosswordUpdater withListeners(final SolverListener... someListeners) {
        listeners.addAll(Arrays.asList(someListeners));
        return this;
    }

    @Override
    public void assign(final Slot variable, final String value) {
        variable.assign(value);
        crossword.probePuzzle().slot(variable.uid()).assign(value);
        crossword.history().addAssignmentRecord(variable);
        crossword.dictionary().invalidateCacheCount(variable);
        listeners.forEach(listener -> listener.onAssignment(variable, value));
    }

    @Override
    public void unassign(final Elimination<Slot, SlotIdentifier> elimination) {
        final Slot variable = elimination.eliminated();
        final String oldValue = variable.unassign();
        crossword.probePuzzle().slot(variable.uid()).unassign();
        crossword.history().removeAssignmentRecord(variable);
        crossword.eliminationSpace().eliminate(variable.uid(), elimination.reasons(), oldValue);
        crossword.dictionary().invalidateCacheCount(variable);
        listeners.forEach(listener -> listener.onUnassignment(variable, oldValue));
    }
}
