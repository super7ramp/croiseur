/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.api.solver;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.crosswords.common.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverPresenter;

import java.util.Collection;
import java.util.Optional;

/**
 * A request to solve a crossword puzzle.
 */
public interface SolveRequest {

    /** Defines how progress should be notified to application for presentation. */
    // TODO expand possibilities of notification, e.g. allows to define a certain timeout; will
    //  impact solver SPI
    enum SolverProgressNotificationMethod {
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
     * The name of the solver to use, if any.
     * <p>
     * If not present, the default solver will be used. The default solver is the first one
     * detected.
     *
     * @return the name of the solver to use.
     */
    Optional<String> solver();

    /**
     * Defines how progress should be notified for presentation.
     *
     * @return the definition of how progress should be notified for presentation
     * @see SolverPresenter#presentProgress
     * @see SolverPresenter#presentSolverInitialisationState
     */
    SolverProgressNotificationMethod progress();
}
