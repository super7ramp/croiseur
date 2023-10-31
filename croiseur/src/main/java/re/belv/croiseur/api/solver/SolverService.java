/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.api.solver;

/**
 * Services pertaining to solving crossword puzzle.
 * <p>
 * Results of the requests are forwarded to the
 * {@link  re.belv.croiseur.spi.presenter.solver.SolverPresenter SolverPresenter}.
 */
public interface SolverService {

    /**
     * Lists available solver providers.
     *
     * @see re.belv.croiseur.spi.presenter.solver.SolverPresenter#presentAvailableSolvers
     * SolverPresenter#presentAvailableSolvers
     */
    void listSolvers();

    /**
     * Solves a puzzle.
     *
     * @param event details about the puzzle to solve
     * @see re.belv.croiseur.spi.presenter.solver.SolverPresenter#presentSolverInitialisationState
     * SolverPresenter#presentSolverInitialisationState
     * @see re.belv.croiseur.spi.presenter.solver.SolverPresenter#presentSolverProgress
     * SolverPresenter#presentSolverProgress
     * @see re.belv.croiseur.spi.presenter.solver.SolverPresenter#presentSolverResult
     * SolverPresenter#presentSolverResult
     * @see re.belv.croiseur.spi.presenter.solver.SolverPresenter#presentSolverError
     * SolverPresenter#presentSolverError
     */
    void solve(final SolveRequest event);
}
