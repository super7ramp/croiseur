package com.gitlab.super7ramp.crosswords.solver.ginsberg.core.sap;

import java.util.Iterator;

/**
 * A constraint satisfaction problem solver.
 */
public interface Solver {

    /**
     * Creates a new solver.
     *
     * @param <VariableT>          type of variable
     * @param <ValueT>             type of value assignable to the variables
     * @param <EliminationReasonT> type of elimination reason
     * @return a new solver
     */
    static <VariableT, ValueT, EliminationReasonT> Solver create(
            final ProblemStateUpdater<VariableT, ValueT, EliminationReasonT> aProblem,
            final Iterator<VariableT> aVariableIterator,
            final CandidateChooser<VariableT, ValueT> aCandidateChooser,
            final Backtracker<VariableT, EliminationReasonT> aBacktracker) {
        return new SolverImpl<>(aProblem, aVariableIterator, aCandidateChooser, aBacktracker);
    }

    /**
     * Solve a problem.
     *
     * @return {@code true} iff solver loop has terminated without error
     * @throws InterruptedException if interrupted while solving
     */
    boolean solve() throws InterruptedException;
}
