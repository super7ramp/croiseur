package com.gitlab.super7ramp.crosswords.api.solver;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.crosswords.spi.solver.ProgressListener;
import com.gitlab.super7ramp.crosswords.spi.solver.PuzzleDefinition;

import java.util.Collection;

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
     * The dictionaries to use.
     * <p>
     * An empty collection means the default dictionary for the system's locale will be used.
     *
     * @return the identifier of the dictionary to use
     */
    Collection<DictionaryIdentifier> dictionaries();

    /**
     * A solver progress listener.
     *
     * @return a progress listener
     */
    // TODO move this to presentation?
    ProgressListener progressListener();
}
