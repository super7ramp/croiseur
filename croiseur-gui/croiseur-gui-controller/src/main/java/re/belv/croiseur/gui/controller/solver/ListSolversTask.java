/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.controller.solver;

import javafx.concurrent.Task;
import re.belv.croiseur.api.solver.SolverService;

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
