/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.controller.solver;

import com.gitlab.super7ramp.croiseur.api.solver.SolverService;
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

    /** Lists the available solvers. */
    @Command(aliases = {"ls"})
    void list() {
        solverService.listSolvers();
    }
}
