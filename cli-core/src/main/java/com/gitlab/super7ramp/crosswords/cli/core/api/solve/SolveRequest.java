package com.gitlab.super7ramp.crosswords.cli.core.api.solve;

import com.gitlab.super7ramp.crosswords.solver.api.ProgressListener;
import com.gitlab.super7ramp.crosswords.solver.api.PuzzleDefinition;

import java.time.Duration;
import java.util.Optional;

/**
 * A request to solve a crossword puzzle.
 */
public interface SolveRequest {

    /**
     * The puzzle to solve.
     *
     * @return the puzzle to solve
     */
    PuzzleDefinition puzzle();

    /**
     * The identifier of the dictionary to use.
     * <p>
     * {@link Optional#empty()} means the default dictionary for the system's locale will be used.
     *
     * @return the identifier of the dictionary to use
     */
    Optional<String> dictionaryId();

    /**
     * A solver progress listener.
     *
     * @return a progress listener
     */
    ProgressListener progressListener();

    /**
     * The maximum allowed duration for the solver to search for a solution.
     *
     * @return the maximum allowed duration for the solver to search for a solution.
     */
    Duration timeout();
}
