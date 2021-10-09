package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.lib.comparators.Comparators;
import com.gitlab.super7ramp.crosswords.solver.lib.db.WordDatabase;
import com.gitlab.super7ramp.crosswords.solver.lib.util.solver.AbstractSatisfactionProblemSolver;

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
     * Manages the dead-ends (i.e. unassign one or more variables and blacklist their values so that other solutions
     * can be tried).
     */
    private final Backtracker backtracker;

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
        backtracker = new BacktrackerImpl(grid, dictionary);
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
        return backtracker.backtrackFrom(wordVariable);
    }

    @Override
    protected void instantiate(final WordVariable variable, final String value) {
        crosswordProblem.assign(variable.uid(), value);
    }

}
