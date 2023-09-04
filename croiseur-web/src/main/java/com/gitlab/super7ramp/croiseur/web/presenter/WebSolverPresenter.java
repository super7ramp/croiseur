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
import com.gitlab.super7ramp.croiseur.web.model.solver.SolverRequestResponseModel;
import com.gitlab.super7ramp.croiseur.web.model.solver.SolverRun;
import com.gitlab.super7ramp.croiseur.web.model.solver.SolverSessionModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of {@link SolverPresenter} for Croiseur Web.
 */
@Component
public class WebSolverPresenter implements SolverPresenter {

    /** The logger. */
    private static final Logger LOGGER = Logger.getLogger(WebSolverPresenter.class.getName());

    /** The solver request response model. */
    private final SolverRequestResponseModel solverRequestResponseModel;

    /** The solver session model. */
    private final SolverSessionModel solverSessionModel;

    /**
     * Constructs an instance.
     *
     * @param solverRequestResponseModelArg the solver request response model to update
     * @param solverSessionModelArg         the solver session model to update
     */
    public WebSolverPresenter(final SolverRequestResponseModel solverRequestResponseModelArg,
                              final SolverSessionModel solverSessionModelArg) {
        solverRequestResponseModel = solverRequestResponseModelArg;
        solverSessionModel = solverSessionModelArg;
    }

    @Override
    public void presentAvailableSolvers(final List<SolverDescription> solverDescriptions) {
        LOGGER.info(() -> "Received solver descriptions: " + solverDescriptions);
        solverRequestResponseModel.solvers(solverDescriptions);
    }

    @Override
    public void presentSolverInitialisationState(final String solverRun,
                                                 final SolverInitialisationState solverInitialisationState) {
        LOGGER.info(() -> "Received solver initialisation state for run '" + solverRun + "': " +
                          solverInitialisationState);
        if (solverInitialisationState == SolverInitialisationState.STARTED) {
            final SolverRun createdRun = solverSessionModel.newSolverRun(solverRun);
            solverRequestResponseModel.solverRun(createdRun);
        } else {
            solverSessionModel.solverRunStarted(solverRun);
        }
    }

    @Override
    public void presentSolverProgress(final String solverRun, final SolverProgress solverProgress) {
        LOGGER.info(
                () -> "Received solver progress for run '" + solverRun + "': " + solverProgress);
        solverSessionModel.solverRunProgressed(solverRun, solverProgress.completionPercentage());
    }

    @Override
    public void presentSolverResult(final String solverRun, final SolverResult result) {
        LOGGER.info(() -> "Received solver result for run '" + solverRun + "': " + result);
        solverSessionModel.solverRunTerminated(solverRun);
    }

    @Override
    public void presentSolverError(final String solverRun, final String error) {
        LOGGER.warning(() -> "Received solver error for run '" + solverRun + "': " + error);
        solverRequestResponseModel.error(error);
    }

    @Override
    public void presentSolverError(final String error) {
        LOGGER.warning(() -> "Received solver error: " + error);
        solverRequestResponseModel.error(error);
    }
}
