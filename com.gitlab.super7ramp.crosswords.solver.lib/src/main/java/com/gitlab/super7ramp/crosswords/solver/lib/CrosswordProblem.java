package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.grid.VariableIdentifier;

import java.util.Collection;

/**
 * The crossword data.
 */
public interface CrosswordProblem {

    /**
     * @return the variables of this problem
     */
    Collection<WordVariable> variables();

    /**
     * Returns a new {@link CrosswordProblem} updated with the given assignment.
     * <p>
     * This can be seen as a read-only version of {@link #assign(VariableIdentifier, String)}.
     *
     * @param variableIdentifier identifier of the updated variable
     * @param value              value of the updated variable
     * @return a new {@link CrosswordProblem} updated with the given assignment.
     */
    CrosswordProblem probe(final VariableIdentifier variableIdentifier, final String value);

    /**
     * Updates the grid with the given assignment.
     *
     * @param variableIdentifier identifier of the updated variable
     * @param value              value of the updated variable
     */
    void assign(final VariableIdentifier variableIdentifier, final String value);
}
