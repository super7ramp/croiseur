/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.presenter;

import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverDescription;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverInitialisationState;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverProgress;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverResult;
import com.gitlab.super7ramp.croiseur.web.session.model.SolverSessionModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of {@link SolverPresenter} for Croiseur Web.
 */
@Component
public final class WebSolverPresenter implements SolverPresenter {

    /** The logger. */
    private static final Logger LOGGER = Logger.getLogger(WebSolverPresenter.class.getName());

    /** The solver session model. */
    private final SolverSessionModel solverSessionModel;

    /**
     * Constructs an instance.
     *
     * @param solverSessionModelArg the solver session model to update
     */
    public WebSolverPresenter(final SolverSessionModel solverSessionModelArg) {
        solverSessionModel = solverSessionModelArg;
    }

    @Override
    public void presentAvailableSolvers(final List<SolverDescription> solverDescriptions) {
        LOGGER.info(() -> "Received solver descriptions: " + solverDescriptions);
        solverSessionModel.solvers(solverDescriptions);
    }

    @Override
    public void presentSolverInitialisationState(
            final SolverInitialisationState solverInitialisationState) {
        LOGGER.info(() -> "Received solver initialisation state: " + solverInitialisationState);
    }

    @Override
    public void presentSolverProgress(final SolverProgress solverProgress) {
        LOGGER.info(() -> "Received solver progress: " + solverProgress);
    }

    @Override
    public void presentSolverResult(final SolverResult result) {
        LOGGER.info(() -> "Received solver result: " + result);
    }

    @Override
    public void presentSolverError(final String error) {
        LOGGER.warning(() -> "Received solver error: " + error);
    }
}