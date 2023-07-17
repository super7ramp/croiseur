/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.controller.solver;

import com.gitlab.super7ramp.croiseur.api.solver.SolverService;
import com.gitlab.super7ramp.croiseur.cli.status.Status;
import picocli.CommandLine.Command;

/**
 * The 'solver' command: Solve crosswords and list available solvers.
 */
@Command(name = "solver")
public final class SolverCommand {

    /** The solver service. */
    private final SolverService solverService;

    /**
     * Constructs an instance.
     *
     * @param solverServiceArg the solver service
     */
    public SolverCommand(final SolverService solverServiceArg) {
        solverService = solverServiceArg;
    }

    /**
     * Lists the available solvers.
     *
     * @return the error status
     */
    @Command(aliases = {"ls"})
    int list() {
        solverService.listSolvers();
        return Status.getAndReset();
    }
}
