package com.gitlab.super7ramp.crosswords.solver.ginsberg.core.sap;

/**
 * Apply state changes.
 *
 * @param <VariableT>          the variable type
 * @param <ValueT>             the value type
 * @param <EliminationReasonT> the elimination reason type
 */
public interface ProblemStateUpdater<VariableT, ValueT, EliminationReasonT> {

    /**
     * Assign a variable.
     *
     * @param variable the variable to assign
     * @param value    the value
     */
    void assign(final VariableT variable, final ValueT value);

    /**
     * Unassign a variable.
     * <p>
     * No-op if given variable is not assigned.
     *
     * @param elimination the variable to unassign and its reason
     */
    void unassign(final Elimination<VariableT, EliminationReasonT> elimination);

}
