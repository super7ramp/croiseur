/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.solver;

import com.gitlab.super7ramp.croiseur.api.solver.SolveRequest;
import com.gitlab.super7ramp.croiseur.api.solver.SolverService;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordSolverViewModel;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;

import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controls the solver.
 */
public final class SolverController {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(SolverController.class.getName());

    /** The solver service. */
    private final SolverService solverService;

    /** The backing executor. */
    private final Executor executor;

    /**
     * A {@link Service} calling {@link SolverService#solve(SolveRequest)} asynchronously, using
     * {@link #executor}.
     * <p>Reason why {@link Service } is preferred over direct use of the executor is that the
     * solver call may take a really long time to complete and need to be stoppable/restartable
     * by user - {@link Service} provides just that.
     */
    private final Service<Void> solver;

    /**
     * Constructs an instance.
     *
     * @param crosswordSolverViewModel the solver view model
     * @param solverServiceArg         the solver service
     * @param executorArg              the backing executor
     */
    public SolverController(final CrosswordSolverViewModel crosswordSolverViewModel,
                            final SolverService solverServiceArg, final Executor executorArg) {

        executor = executorArg;
        solverService = solverServiceArg;

        solver = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new SolveTask(crosswordSolverViewModel.crosswordGridViewModel(),
                        crosswordSolverViewModel.dictionaryViewModel(),
                        crosswordSolverViewModel.solverSelectionViewModel(), solverService);
            }
        };
        // FIXME #43 native solvers don't respond to interruption, have to use the Service default
        //  daemon thread pool for now so that the JVM can exit if the solver is stuck.
        // solver.setExecutor(executor);
        solver.setOnReady(e -> LOGGER.info("Solver ready"));
        solver.setOnRunning(e -> LOGGER.info("Solving"));
        solver.setOnCancelled(e -> LOGGER.info("Solver cancelled"));
        solver.setOnSucceeded(e -> LOGGER.info("Solver finished"));
        solver.setOnFailed(e -> LOGGER.log(Level.WARNING, "Solver failed",
                e.getSource().getException()));
        crosswordSolverViewModel.solverRunning().bind(solver.runningProperty());
    }

    /**
     * Starts the solver.
     */
    public void startSolver() {
        if (solver.getState() != Worker.State.READY) {
            stopSolver();
        }
        solver.start();
    }

    /**
     * Stops the solver.
     */
    public void stopSolver() {
        solver.cancel();
        solver.reset();
    }

    /**
     * Lists the available solvers.
     */
    public void listSolvers() {
        final ListSolversTask listSolversTask = new ListSolversTask(solverService);
        listSolversTask.setOnFailed(e -> LOGGER.log(Level.WARNING, "Failed to list solvers",
                e.getSource().getException()));
        executor.execute(listSolversTask);
    }
}
