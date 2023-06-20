/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.api.solver;

import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;

/**
 * A request to solve a crossword puzzle.
 */
public interface SolveRequest {

    /** Defines how progress should be notified to application for presentation. */
    // TODO expand possibilities of notification, e.g. allows to define a certain refresh frequency; will
    //  impact solver SPI
    enum SolverProgressNotificationMethod {
        /** Progress is never notified for presentation. */
        NONE,
        /** Progress is periodically notified to presentation. */
        PERIODICAL
    }

    /**
     * The grid to solve.
     *
     * @return the grid to solve
     */
    PuzzleGrid grid();

    /**
     * Whether the given {@link #grid()} shall be saved to puzzle repository.
     *
     * @return {@code true} if given {@link #grid()} shall be saved to puzzle repository
     */
    boolean savePuzzle();

    /**
     * The dictionaries to use.
     * <p>
     * An empty collection means the default dictionary for the system's locale will be used.
     *
     * @return the identifier of the dictionary to use
     */
    Collection<DictionaryIdentifier> dictionaries();

    /**
     * The randomness source to use to shuffle the dictionaries.
     * <p>
     * The search for solutions depends on the order the words are read from the dictionaries.
     * Shuffling dictionary is likely to lead to a different order of solutions.
     *
     * @return The randomness source to use to shuffle the dictionaries; An empty source means the
     * dictionaries will <em>not</em> be shuffled
     */
    Optional<Random> dictionariesShuffle();

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
     * @see SolverPresenter#presentSolverProgress
     * @see SolverPresenter#presentSolverInitialisationState
     */
    SolverProgressNotificationMethod progress();
}
