/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.api.solver;

/**
 * Services pertaining to solving crossword puzzle.
 * <p>
 * Results of the requests are forwarded to the
 * {@link com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter SolverPresenter}
 */
public interface SolverService {

    /**
     * Lists available solver providers.
     *
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter#presentAvailableSolvers
     * SolverPresenterpresentAvailableSolvers
     */
    void listSolvers();

    /**
     * Solves a puzzle.
     *
     * @param event details about the puzzle to solve
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter#presentSolverInitialisationState
     * SolverPresenter.presentSolverInitialisationState
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter#presentSolverProgress
     * SolverPresenter.presentSolverProgress
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter#presentSolverResult
     * SolverPresenter.presentSolverResult
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter#presentSolverError
     * SolverPresenter.presentSolverError
     */
    void solve(final SolveRequest event);
}
