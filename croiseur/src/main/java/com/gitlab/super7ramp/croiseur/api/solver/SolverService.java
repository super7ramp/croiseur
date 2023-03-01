/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.api.solver;

import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverInitialisationState;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverProgress;
import com.gitlab.super7ramp.croiseur.spi.solver.SolverResult;

import java.util.List;

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
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter#presentAvailableSolvers(List)
     * SolverPresenterpresentAvailableSolvers
     */
    void listSolvers();

    /**
     * Solves a puzzle.
     *
     * @param event details about the puzzle to solve
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter#presentSolverInitialisationState(SolverInitialisationState)
     * SolverPresenter.presentSolverInitialisationState
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter#presentProgress(SolverProgress)
     * SolverPresenter.presentProgress
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter#presentResult(SolverResult)
     * SolverPresenter.presentResult
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter#presentSolverError(String)
     * SolverPresenter.presentSolverError
     */
    void solve(final SolveRequest event);
}
