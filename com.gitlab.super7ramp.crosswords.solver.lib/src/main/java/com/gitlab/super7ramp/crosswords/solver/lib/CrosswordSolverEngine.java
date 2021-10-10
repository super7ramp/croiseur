package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.lib.comparators.Comparators;
import com.gitlab.super7ramp.crosswords.solver.lib.db.WordDatabase;
import com.gitlab.super7ramp.crosswords.solver.lib.util.solver.AbstractSatisfactionProblemSolverEngine;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of {@link AbstractSatisfactionProblemSolverEngine} for crosswords.
 */
public final class CrosswordSolverEngine extends AbstractSatisfactionProblemSolverEngine<Slot, String> {

    /**
     * Manages the selection of variable to instantiate.
     */
    private final Iterator<Slot> variables;

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
    private final Puzzle puzzle;

    /**
     * Constructor.
     *
     * @param grid       the grid
     * @param dictionary the dictionary
     */
    CrosswordSolverEngine(
            final ProbablePuzzle grid,
            final WordDatabase dictionary) {
        puzzle = grid;
        variables = new SlotIterator(grid, Comparators.byNumberOfCandidates(dictionary));
        candidateChooser = new CandidateChooser(grid, dictionary);
        backtracker = new BacktrackerImpl(grid, dictionary);
    }

    @Override
    protected Iterator<Slot> variables() {
        return variables;
    }

    @Override
    protected Optional<String> candidate(final Slot wordVariable) {
        return candidateChooser.find(wordVariable);
    }

    @Override
    protected Set<Slot> backtrackFrom(final Slot wordVariable) {
        return backtracker.backtrackFrom(wordVariable);
    }

}
