/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.model;

import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverDescription;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The solver request response model: All data relative to the solver service and relevant only for
 * the lifetime of a request.
 */
@Component
@RequestScope
public class SolverRequestResponseModel {

    /** The list of solvers queried by the current request. */
    private final List<SolverDescription> solvers;

    /** The solver runs created or queried by the current request. */
    private final List<SolverRun> solverRuns;

    /**
     * Constructs an instance.
     */
    public SolverRequestResponseModel() {
        solvers = new ArrayList<>();
        solverRuns = new ArrayList<>();
    }

    /**
     * Retrieves the list of solvers.
     *
     * @return the solvers
     */
    public Iterable<SolverDescription> solvers() {
        return solvers;
    }

    /**
     * Sets the list of solvers.
     *
     * @param solverDescriptions the list of solvers
     */
    public void solvers(final List<SolverDescription> solverDescriptions) {
        solvers.addAll(solverDescriptions);
    }

    /**
     * Adds the given solver to the list of runs created or queried by the current request.
     *
     * @param run the solver run created or queried by the current request
     */
    public void solverRuns(final SolverRun run) {
        solverRuns.add(run);
    }

    /**
     * The first solver run created or queried by the current request, if any.
     *
     * @return the solver run created or queried by the current request, if any
     */
    public Optional<SolverRun> solverRun() {
        return solverRuns.stream().findFirst();
    }
}
