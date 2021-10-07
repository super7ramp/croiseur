package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.db.WordDatabase;
import com.gitlab.super7ramp.crosswords.solver.lib.comparators.Comparators;
import com.gitlab.super7ramp.crosswords.util.solver.AbstractSatisfactionProblemSolver;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of {@link AbstractSatisfactionProblemSolver} for crosswords.
 */
public final class CrosswordSolver extends AbstractSatisfactionProblemSolver<WordVariable, String> {

    /**
     * Manages the selection of variable to instantiate.
     */
    private final Iterator<WordVariable> variables;

    /**
     * Manages the instantiation of a variable (i.e. its assignment).
     */
    private final CandidateChooser candidateChooser;

    /**
     * The grid.
     */
    private final CrosswordProblem crosswordProblem;

    /**
     * Constructor.
     *
     * @param grid       the grid
     * @param dictionary the dictionary
     */
    CrosswordSolver(
            final CrosswordProblem grid,
            final WordDatabase dictionary) {
        crosswordProblem = grid;
        variables = new WordVariableIterator(grid, Comparators.byNumberOfCandidates(dictionary));
        candidateChooser = new CandidateChooser(grid, dictionary);
    }

    @Override
    protected Iterator<WordVariable> variables() {
        return variables;
    }

    @Override
    protected Optional<String> candidate(final WordVariable wordVariable) {
        return candidateChooser.find(wordVariable);
    }

    @Override
    protected Set<WordVariable> backtrackFrom(final WordVariable wordVariable) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void instantiate(final WordVariable variable, final String value) {
        crosswordProblem.assign(variable.uid(), value);
    }

}
