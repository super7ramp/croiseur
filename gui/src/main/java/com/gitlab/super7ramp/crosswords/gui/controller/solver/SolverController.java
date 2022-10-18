package com.gitlab.super7ramp.crosswords.gui.controller.solver;

import com.gitlab.super7ramp.crosswords.api.solver.SolverUsecase;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordSolverViewModel;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;

import java.util.logging.Logger;

/**
 * Controls the solver.
 */
public final class SolverController {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(SolverController.class.getName());

    /** The worker executing the solver task. */
    private final Service<Void> solverService;

    /**
     * Constructs an instance.
     *
     * @param crosswordSolverViewModel the solver view model
     * @param usecase                  the "solve crossword" usecase
     */
    public SolverController(final CrosswordSolverViewModel crosswordSolverViewModel,
                            final SolverUsecase usecase) {
        solverService = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new SolveTask(crosswordSolverViewModel.crosswordViewModel(),
                        crosswordSolverViewModel.dictionaryViewModel(), usecase);
            }
        };
        solverService.setOnReady(e -> LOGGER.info("Solver worker is ready"));
        solverService.setOnRunning(e -> LOGGER.info("Solving in progress"));
        solverService.setOnCancelled(e -> LOGGER.info("Solving cancelled"));
        solverService.setOnSucceeded(e -> LOGGER.info("Solving succeeded"));
        solverService.setOnFailed(e -> LOGGER.info("Solving failed"));
        crosswordSolverViewModel.solverRunning().bind(solverService.runningProperty());
    }

    /**
     * Starts the solver.
     */
    public void start() {
        if (solverService.getState() != Worker.State.READY) {
            stop();
        }
        solverService.start();
    }

    /**
     * Stops the solver. Does nothing if
     */
    public void stop() {
        solverService.cancel();
        solverService.reset();
    }

}
