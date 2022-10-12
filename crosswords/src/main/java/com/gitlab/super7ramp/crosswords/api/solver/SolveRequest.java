package com.gitlab.super7ramp.crosswords.api.solver;

import com.gitlab.super7ramp.crosswords.spi.solver.ProgressListener;
import com.gitlab.super7ramp.crosswords.spi.solver.PuzzleDefinition;

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
    // TODO move this to presentation?
    ProgressListener progressListener();
}
