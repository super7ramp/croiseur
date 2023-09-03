/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.api.solver;

import re.belv.croiseur.api.dictionary.DictionaryIdentifier;
import re.belv.croiseur.common.puzzle.PuzzleGrid;

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
     * The grid to solve.
     *
     * @return the grid to solve
     */
    PuzzleGrid grid();

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
     * The name of the solver run triggered by this request.
     * <p>
     * It can be used as an identifier by caller to manage several solver runs in parallel. This
     * string will be passed as argument of the
     * {@link re.belv.croiseur.spi.presenter.solver.SolverPresenter SolverPresenter} methods.
     * <p>
     * Note that solver service does not maintain a memory of the solver runs itself. This means
     * that solver service will <em>not</em> perform any validation on this field, i.e. it is
     * possible for two runs to use the same identifier at the same time if caller does not prevent
     * it.
     *
     * @return the name of the solver run
     */
    default String solverRun() {
        return "";
    }

    /**
     * Defines how progress should be notified for presentation.
     *
     * @return the definition of how progress should be notified for presentation
     * @see re.belv.croiseur.spi.presenter.solver.SolverPresenter#presentSolverProgress
     * SolverPresenter#presentSolverProgress
     * @see re.belv.croiseur.spi.presenter.solver.SolverPresenter#presentSolverInitialisationState
     * SolverPresenter#presentSolverInitialisationState
     */
    SolverProgressNotificationMethod progress();

    /**
     * Whether to generate clues if solver finds a solution.
     * <p>
     * The first clue provider found is used.
     *
     * @return {@code true} if clues shall be generated upon solver success
     */
    boolean withClues();

    /**
     * Whether the given {@link #grid()} shall be saved as a new record in the puzzle repository.
     *
     * @return {@code true} if given {@link #grid()} shall be saved as a new record in the puzzle
     * repository
     */
    boolean savePuzzle();
}
