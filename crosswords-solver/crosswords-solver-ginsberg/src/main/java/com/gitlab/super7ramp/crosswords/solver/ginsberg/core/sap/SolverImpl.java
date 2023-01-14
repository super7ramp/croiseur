/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.solver.ginsberg.core.sap;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Base abstraction for SAP solving
 * <p>
 * This class implements a solver, provided:
 * <ul>
 *     <li>an {@link Iterator iterator} to iterate on the variables</li>
 *     <li>a {@link CandidateChooser <VariableT,ValueT> candidate chooser} to select the best
 *     next variable</li>
 *     <li>a {@link Backtracker backtracking strategy} to handle the dead-ends</li>
 *     <li>a {@link ProblemStateUpdater problem state updater} to commit the changes to the
 *     problem</li>
 * </ul>
 *
 * @param <VariableT>          type of variable
 * @param <ValueT>             type of value assignable to the variables
 * @param <EliminationReasonT> type of elimination reason
 */
final class SolverImpl<VariableT, ValueT, EliminationReasonT> implements Solver {

    /** Problem state. */
    private final ProblemStateUpdater<VariableT, ValueT, EliminationReasonT> problem;

    /** Finds the best next candidate. */
    private final Iterator<VariableT> variableIterator;

    /** Finds the best solution for a candidate. */
    private final CandidateChooser<VariableT, ValueT> candidateChooser;

    /** Finds the best solution to solve a dead-end. */
    private final Backtracker<VariableT, EliminationReasonT> backtracker;

    /**
     * Constructor.
     *
     * @param aVariableIterator the variable iteration heuristics
     * @param aCandidateChooser the candidate choice heuristics
     * @param aBacktracker      the backtracking heuristics
     */
    SolverImpl(final ProblemStateUpdater<VariableT, ValueT, EliminationReasonT> aProblem,
               final Iterator<VariableT> aVariableIterator, final CandidateChooser<VariableT,
            ValueT> aCandidateChooser,
               final Backtracker<VariableT, EliminationReasonT> aBacktracker) {
        problem = Objects.requireNonNull(aProblem);
        variableIterator = Objects.requireNonNull(aVariableIterator);
        candidateChooser = Objects.requireNonNull(aCandidateChooser);
        backtracker = Objects.requireNonNull(aBacktracker);
    }

    @Override
    public boolean solve() throws InterruptedException {

        boolean hasSolution = true;
        while (!Thread.currentThread()
                      .isInterrupted() && variableIterator.hasNext() && hasSolution) {

            final VariableT variable = variableIterator.next();
            final Optional<ValueT> candidate = candidateChooser.find(variable);

            if (candidate.isPresent()) {
                problem.assign(variable, candidate.get());
            } else {
                final List<Elimination<VariableT, EliminationReasonT>> toUnassign =
                        backtracker.backtrackFrom(variable);
                toUnassign.forEach(problem::unassign);
                hasSolution = !toUnassign.isEmpty();
            }
        }

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("Solver interrupted");
        }

        return hasSolution;
    }
}
