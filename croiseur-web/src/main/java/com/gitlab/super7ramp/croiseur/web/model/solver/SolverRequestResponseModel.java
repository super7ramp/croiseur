/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.model.solver;

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

    /** The errors. */
    private final List<String> errors;

    /**
     * Constructs an instance.
     */
    public SolverRequestResponseModel() {
        solvers = new ArrayList<>();
        solverRuns = new ArrayList<>();
        errors = new ArrayList<>();
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
    public void solverRun(final SolverRun run) {
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

    /**
     * Adds the given error to the list of errors raised by the current request.
     *
     * @param error the error
     */
    public void error(final String error) {
        errors.add(error);
    }

    /**
     * A text containing all the errors raised by the current request. There is one line per error.
     *
     * @return a text containing all the errors raised by the current request; empty if no error
     * raised by current request
     */
    public String error() {
        return String.join("\n", errors);
    }
}
