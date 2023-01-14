/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.solver.ginsberg.state;

import com.gitlab.super7ramp.crosswords.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.core.SlotIdentifier;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.core.sap.Elimination;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.core.sap.ProblemStateUpdater;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.listener.SolverListener;

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
