/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.solver;

import com.gitlab.super7ramp.croiseur.api.solver.SolverService;
import javafx.concurrent.Task;

/**
 * List solvers task.
 */
final class ListSolversTask extends Task<Void> {

    /** The solver service. */
    private final SolverService solverService;

    /**
     * Constructs an instance.
     *
     * @param solverServiceArg the solver service
     */
    ListSolversTask(final SolverService solverServiceArg) {
        solverService = solverServiceArg;
    }

    @Override
    protected Void call() {
        solverService.listSolvers();
        return null;
    }
}
