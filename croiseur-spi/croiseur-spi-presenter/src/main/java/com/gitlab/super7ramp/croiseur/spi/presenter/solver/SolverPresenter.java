/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.presenter.solver;

import com.gitlab.super7ramp.croiseur.spi.solver.SolverResult;

import java.util.List;

/**
 * Solver-related presentation services.
 */
// TODO create specific types so that Presenter SPI does not depend on Solver SPIs
public interface SolverPresenter {

    /**
     * Presents the available solvers.
     * <p>
     * First solver is the preferred solver and is implementation dependent. The other solvers are
     * sorted alphabetically by their name.
     *
     * @param solverDescriptions the solver descriptions
     */
    void presentAvailableSolvers(final List<SolverDescription> solverDescriptions);

    /**
     * Presents the solver initialisation state.
     *
     * @param solverInitialisationState the solver initialisation state
     */
    void presentSolverInitialisationState(final SolverInitialisationState solverInitialisationState);

    /**
     * Presents the solving progress.
     *
     * @param progress the completion percentage of the solving
     */
    void presentProgress(final SolverProgress progress);

    /**
     * Presents the result of a crossword solving request.
     *
     * @param result the solver result
     */
    // TODO create specific solver result type so that Presenter SPI does not depend on Solver SPI?
    void presentResult(final SolverResult result);

    /**
     * Presents an error from the solver.
     *
     * @param error the error
     */
    // TODO error should be a dedicated type
    void presentSolverError(final String error);

}
