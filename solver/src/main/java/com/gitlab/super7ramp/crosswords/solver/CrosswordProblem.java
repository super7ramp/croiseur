package main.java.com.gitlab.super7ramp.crosswords.solver;

import main.java.com.gitlab.super7ramp.crosswords.grid.VariableIdentifier;

import java.util.Collection;

/**
 * A container for the variables.
 */
public interface CrosswordProblem {

    /**
     * @return the variables of this problem
     */
    Collection<WordVariable> variables();

    /**
     * Returns a new {@link CrosswordProblem} updated with the given assignment.
     *
     * @param variableIdentifier identifier of the updated variable
     * @param value value of the updated variable
     * @return a new {@link CrosswordProblem} updated with the given assignment.
     */
    CrosswordProblem assign(final VariableIdentifier variableIdentifier, final String value);

}
