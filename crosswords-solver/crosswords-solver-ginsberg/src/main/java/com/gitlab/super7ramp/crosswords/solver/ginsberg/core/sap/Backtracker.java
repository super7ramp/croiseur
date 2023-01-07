package com.gitlab.super7ramp.crosswords.solver.ginsberg.core.sap;

import java.util.List;

/**
 * Strategy to apply when dead-end is reached.
 *
 * @param <VariableT> the variable type
 * @param <EliminationReasonT> the elimination reason type
 */
public interface Backtracker<VariableT, EliminationReasonT> {

    /**
     * Determines the variable(s) to unassign to solve the dead-end reached on given variable,
     * accompanied by a reason.
     *
     * @param variable unassignable variable
     * @return the variable(s) that should be unassigned and why; An empty list if none could be
     * found
     */
    List<Elimination<VariableT, EliminationReasonT>> backtrackFrom(final VariableT variable);
}
