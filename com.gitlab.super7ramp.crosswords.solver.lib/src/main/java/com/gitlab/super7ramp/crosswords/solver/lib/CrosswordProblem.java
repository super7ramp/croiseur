package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.lib.grid.VariableIdentifier;

import java.util.Collection;
import java.util.Set;

/**
 * Read/write crossword puzzle state.
 * <p>
 * TODO split probe in a dedicated interface and implementation
 */
public interface CrosswordProblem {

    /**
     * Read-only access to the variables of this problem.
     *
     * @return the variables of this problem
     */
    Collection<WordVariable> variables();

    /**
     * Access to variable with given ID.
     *
     * @param id variable id
     * @return the corresponding {@link WordVariable}
     * @throws IllegalArgumentException if no variables matches the given ID
     */
    WordVariable variable(VariableIdentifier id);

    /**
     * Read-only access to the constraints of this problem.
     *
     * @return the constraints of this problem
     */
    Set<CrosswordConstraint> constraints();

    /**
     * Returns a new {@link CrosswordProblem} updated with the given assignment.
     * <p>
     * This can be seen as a read-only version of {@link #assign(VariableIdentifier, String)}.
     *
     * @param variableIdentifier identifier of the updated variable
     * @param value              value of the updated variable
     * @return a new {@link CrosswordProblem} updated with the given assignment.
     */
    CrosswordProblem probeAssignment(final VariableIdentifier variableIdentifier, final String value);

    /**
     * Updates the grid with the given assignment.
     *
     * @param variableIdentifier identifier of the updated variable
     * @param value              value of the updated variable
     */
    void assign(final VariableIdentifier variableIdentifier, final String value);

    /**
     * Removes the value of the given variable.
     *
     * @param variableIdentifier the identifier of the variable whose value should be cleared
     */
    void unassign(final VariableIdentifier variableIdentifier);
}
