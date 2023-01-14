/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.gui.controller.solver;

import com.gitlab.super7ramp.crosswords.api.solver.SolverService;
import com.gitlab.super7ramp.crosswords.gui.view.model.CrosswordSolverViewModel;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controls the solver.
 */
public final class SolverController {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(SolverController.class.getName());

    /** The worker executing the solver task. */
    private final Service<Void> worker;

    /**
     * Constructs an instance.
     *
     * @param crosswordSolverViewModel the solver view model
     * @param solverService            the "solve crossword" usecase
     */
    public SolverController(final CrosswordSolverViewModel crosswordSolverViewModel,
                            final SolverService solverService) {
        worker = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new SolveTask(crosswordSolverViewModel.crosswordGridViewModel(),
                        crosswordSolverViewModel.dictionaryViewModel(), solverService);
            }
        };
        worker.setOnReady(e -> LOGGER.info("Solver ready"));
        worker.setOnRunning(e -> LOGGER.info("Solving"));
        worker.setOnCancelled(e -> LOGGER.info("Solver cancelled"));
        worker.setOnSucceeded(e -> LOGGER.info("Solver finished"));
        worker.setOnFailed(e -> LOGGER.log(Level.WARNING, "Solver failed",
                e.getSource().getException()));
        crosswordSolverViewModel.solverRunning().bind(worker.runningProperty());
    }

    /**
     * Starts the solver.
     */
    public void start() {
        if (worker.getState() != Worker.State.READY) {
            stop();
        }
        worker.start();
    }

    /**
     * Stops the solver.
     */
    public void stop() {
        worker.cancel();
        worker.reset();
    }

}
