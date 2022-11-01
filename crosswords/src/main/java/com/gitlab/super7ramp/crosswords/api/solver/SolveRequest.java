package com.gitlab.super7ramp.crosswords.api.solver;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.crosswords.spi.presenter.SolverInitialisationState;
import com.gitlab.super7ramp.crosswords.spi.solver.PuzzleDefinition;

import java.util.Collection;

/**
 * A request to solve a crossword puzzle.
 */
public interface SolveRequest {

    /** Defines how progress should be notified to application for presentation. */
    // TODO expand possibilities of notification, e.g. allows to define a certain timeout; will
    //  impact solver SPI
    enum SolverProgressNotificationKind {
        /** Progress is never notified for presentation. */
        NONE,
        /** Progress is periodically notified to presentation. */
        PERIODICAL
    }

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
     * Defines how progress should be notified for presentation.
     *
     * @return the definition of how progress should be notified for presentation
     * @see com.gitlab.super7ramp.crosswords.spi.presenter.SolverPresenter#presentProgress(short)
     * @see com.gitlab.super7ramp.crosswords.spi.presenter.SolverPresenter#presentSolverInitialisationState(SolverInitialisationState)
     */
    SolverProgressNotificationKind progress();
}
