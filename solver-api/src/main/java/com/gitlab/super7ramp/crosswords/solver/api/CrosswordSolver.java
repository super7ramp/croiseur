package com.gitlab.super7ramp.crosswords.solver.api;

/**
 * A crossword solver.
 */
public interface CrosswordSolver {

    /**
     * Solve the given puzzle, using the given dictionary.
     *
     * @param puzzle     the puzzle to solve
     * @param dictionary the dictionary to use
     * @return the result
     * @throws InterruptedException if interrupted while solving
     */
    SolverResult solve(final PuzzleDefinition puzzle, final Dictionary dictionary) throws InterruptedException;

}
