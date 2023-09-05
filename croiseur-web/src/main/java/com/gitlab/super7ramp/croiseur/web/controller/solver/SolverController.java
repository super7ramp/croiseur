/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.controller.solver;

import com.gitlab.super7ramp.croiseur.api.solver.SolveRequest;
import com.gitlab.super7ramp.croiseur.api.solver.SolverService;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverDescription;
import com.gitlab.super7ramp.croiseur.web.concurrent.RequestAwareRunnable;
import com.gitlab.super7ramp.croiseur.web.model.solver.SolverRequestResponseModel;
import com.gitlab.super7ramp.croiseur.web.model.solver.SolverRun;
import com.gitlab.super7ramp.croiseur.web.model.solver.SolverSessionModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executor;

/**
 * Controller of the {@link SolverService}.
 */
@RestController
@RequestMapping("/solvers")
public class SolverController {

    /** The service that this controller calls. */
    private final SolverService solverService;

    /** The solver request response model, where the request state is stored. */
    private final SolverRequestResponseModel solverRequestResponseModel;

    /** The solver session model, where the session state is stored. */
    private final SolverSessionModel solverSessionModel;

    /** Executor for asynchronous operations. */
    private final Executor executor;

    /**
     * Constructs an instance.
     *
     * @param solverServiceArg              the solver service
     * @param solverRequestResponseModelArg the solver request response model
     * @param solverSessionModelArg         the solver session model
     * @param executorArg                   the executor, for asynchronous operations
     */
    public SolverController(final SolverService solverServiceArg,
                            final SolverRequestResponseModel solverRequestResponseModelArg,
                            final SolverSessionModel solverSessionModelArg,
                            final Executor executorArg) {
        solverService = solverServiceArg;
        solverRequestResponseModel = solverRequestResponseModelArg;
        solverSessionModel = solverSessionModelArg;
        executor = executorArg;
    }

    /**
     * Lists all available solvers.
     *
     * @return all the available solvers
     */
    @GetMapping({"", "/"})
    public Iterable<SolverDescription> getSolvers() {
        solverService.listSolvers();
        return solverRequestResponseModel.solvers();
    }

    /**
     * Lists all solver runs.
     *
     * @return the solver runs
     */
    @GetMapping("/runs")
    public Iterable<SolverRun> getSolverRuns() {
        return solverSessionModel.solverRuns();
    }

    /**
     * Gets the solver run with given name.
     *
     * @return the solver run with given name
     */
    @GetMapping("/runs/{name}")
    public ResponseEntity<SolverRun> getSolverRun(@PathVariable("name") final String name) {
        final Optional<SolverRun> solverRun = solverSessionModel.solverRun(name);
        return ResponseEntity.of(solverRun);
    }

    /**
     * Starts a solver run.
     *
     * @param grid the grid to solve
     * @return 201 with the URI to the created solver run resource, or 500 with the error message
     */
    @PostMapping("/runs")
    public HttpEntity<Object> run(@RequestBody final PuzzleGrid grid) {
        final var runName = UUID.randomUUID().toString();
        final var solveRequest = new WebSolveRequest(grid, runName);
        asyncRunSolver(solveRequest);
        return solverRequestResponseModel.solverRun()
                                         .map(run -> ResponseEntity.created(toUri(run)).build())
                                         .orElseGet(() -> ResponseEntity.internalServerError()
                                                                        .body(solverRequestResponseModel.error()));
    }

    @DeleteMapping("/runs")
    public void deleteAll() {
        solverSessionModel.clear();
    }

    /**
     * Launches the asynchronous processing of the given solve request.
     *
     * @param solveRequest the solve request to process
     */
    private void asyncRunSolver(final SolveRequest solveRequest) {
        final var task = RequestAwareRunnable.wrap(() -> solverService.solve(solveRequest));
        // TODO timeout
        executor.execute(task);
    }

    /**
     * Returns the URI of the created solver run resource.
     *
     * @param run the solver run resource
     * @return the URI of the solver run resource
     */
    private URI toUri(final SolverRun run) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{name}")
                .buildAndExpand(run.name())
                .toUri();
    }
}
