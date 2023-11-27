/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.web.controller.solver;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import re.belv.croiseur.api.solver.SolveRequest;
import re.belv.croiseur.api.solver.SolverService;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.spi.presenter.solver.SolverDescription;
import re.belv.croiseur.web.concurrent.RequestAwareRunnable;
import re.belv.croiseur.web.model.solver.SolverRequestResponseModel;
import re.belv.croiseur.web.model.solver.SolverRun;
import re.belv.croiseur.web.model.solver.SolverSessionModel;

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

    @GetMapping
    @Operation(summary = "Lists all available solvers")
    @ApiResponse(responseCode = "200", description = "All solver runs")
    public Iterable<SolverDescription> getSolvers() {
        solverService.listSolvers();
        return solverRequestResponseModel.solvers();
    }

    @GetMapping("/runs")
    @Operation(summary = "Lists all solver runs")
    @ApiResponse(responseCode = "200", description = "All solver runs")
    public Iterable<SolverRun> getSolverRuns() {
        return solverSessionModel.solverRuns();
    }

    @GetMapping("/runs/{name}")
    @Operation(summary = "Gets the solver run with given name")
    @ApiResponse(responseCode = "200", description = "Solver run found")
    @ApiResponse(responseCode = "404", description = "Solver run not found", content = @Content)
    public ResponseEntity<SolverRun> getSolverRun(
            @Parameter(description = "Name of the solver run to search")
            @PathVariable("name") final String name) {
        final Optional<SolverRun> solverRun = solverSessionModel.solverRun(name);
        return ResponseEntity.of(solverRun);
    }

    @PostMapping("/runs")
    @Operation(summary = "Starts a solver run")
    @ApiResponse(responseCode = "201", description = "The URI to the created solver run resource")
    public HttpEntity<Object> run(
            @Parameter(description = "The grid to solve") @RequestBody final PuzzleGrid grid) {
        final var runName = UUID.randomUUID().toString();
        final var solveRequest = new WebSolveRequest(grid, runName);
        asyncRunSolver(solveRequest);
        return solverRequestResponseModel.solverRun()
                                         .map(run -> ResponseEntity.created(toUri(run)).build())
                                         .orElseGet(() -> ResponseEntity.internalServerError()
                                                                        .body(solverRequestResponseModel.error()));
    }

    @DeleteMapping("/runs")
    @Operation(summary = "Deletes all solver runs")
    @ApiResponse(responseCode = "200", description = "All solver runs deleted")
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
