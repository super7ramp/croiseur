/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli.controller.solver;

import picocli.CommandLine.Command;
import re.belv.croiseur.api.solver.SolverService;
import re.belv.croiseur.cli.status.Status;

/** The 'solver' command: Solve crosswords and list available solvers. */
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
