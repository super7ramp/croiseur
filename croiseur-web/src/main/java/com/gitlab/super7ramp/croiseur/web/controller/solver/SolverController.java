/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.controller.solver;

import com.gitlab.super7ramp.croiseur.api.solver.SolverService;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverDescription;
import com.gitlab.super7ramp.croiseur.web.session.model.SolverSessionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller of the {@link SolverService}.
 */
@RestController
@RequestMapping("/solvers")
public final class SolverController {

    /** The service that this controller calls. */
    private final SolverService solverService;

    /** The solver session model, where the session state is stored. */
    private final SolverSessionModel solverSessionModel;

    /**
     * Constructs an instance.
     *
     * @param solverServiceArg      the solver service
     * @param solverSessionModelArg the solver session model
     */
    public SolverController(final SolverService solverServiceArg,
                            final SolverSessionModel solverSessionModelArg) {
        solverService = solverServiceArg;
        solverSessionModel = solverSessionModelArg;
    }

    /**
     * Lists all available solvers.
     *
     * @return all the available solvers
     */
    @GetMapping({"", "/"})
    public Iterable<SolverDescription> getSolvers() {
        solverService.listSolvers();
        return solverSessionModel.solvers();
    }
}
