/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.cli.controller.solver;

import com.gitlab.super7ramp.crosswords.api.solver.SolverService;
import picocli.CommandLine.Command;

/**
 * The 'solver' command.
 */
@Command(name = "solver", description = "Solve crosswords and list available solvers",
        synopsisSubcommandLabel = "COMMAND" /* instead of [COMMAND], because subcommand is
        mandatory */)
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

    @Command(name = "list", aliases = {"ls"}, description = "List available solvers")
    void list() {
        solverService.listProviders();
    }
}
