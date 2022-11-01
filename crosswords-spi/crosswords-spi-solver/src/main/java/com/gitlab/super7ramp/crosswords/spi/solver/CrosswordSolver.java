package com.gitlab.super7ramp.crosswords.spi.solver;

import com.gitlab.super7ramp.crosswords.common.PuzzleDefinition;

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
    default SolverResult solve(final PuzzleDefinition puzzle, final Dictionary dictionary) throws InterruptedException {
        return solve(puzzle, dictionary, ProgressListener.DUMMY_LISTENER);
    }

    /**
     * Solve the given puzzle, using the given dictionary.
     *
     * @param puzzle           the puzzle to solve
     * @param dictionary       the dictionary to use
     * @param progressListener the progress listener
     * @return the result
     * @throws InterruptedException if interrupted while solving
     */
    SolverResult solve(final PuzzleDefinition puzzle, final Dictionary dictionary,
                       final ProgressListener progressListener) throws InterruptedException;

}
