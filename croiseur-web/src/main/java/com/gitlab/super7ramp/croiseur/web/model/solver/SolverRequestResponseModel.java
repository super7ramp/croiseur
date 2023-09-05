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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * The solver request response model: All data relative to the solver service and relevant only for
 * the lifetime of a request.
 */
@Component
@RequestScope
public class SolverRequestResponseModel {

    /** The list of solvers queried by the current request. */
    private final List<SolverDescription> solvers;

    /** The blocking queue of solver runs created asynchronously by the current request, if any. */
    private final BlockingQueue<SolverRun> solverRun;

    /** The errors. */
    private final List<String> errors;

    /**
     * Constructs an instance.
     */
    public SolverRequestResponseModel() {
        solvers = new ArrayList<>();
        solverRun = new SynchronousQueue<>();
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
     * Adds the given solver to the queue of solver runs created asynchronously by the current
     * request.
     *
     * @param run the solver run created asynchronously by the current request
     */
    public void solverRun(final SolverRun run) {
        try {
            solverRun.put(run);
        } catch (final InterruptedException e) {
            // TODO log, error handling
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * The solver run created asynchronously by the current request.
     *
     * @return solver run created asynchronously by the current request
     */
    public Optional<SolverRun> solverRun() {
        try {
            // TODO extract constant
            return Optional.ofNullable(solverRun.poll(5L, TimeUnit.SECONDS));
        } catch (final InterruptedException e) {
            // TODO log, error handling
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
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
