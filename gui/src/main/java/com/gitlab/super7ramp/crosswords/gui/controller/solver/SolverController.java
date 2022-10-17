package com.gitlab.super7ramp.crosswords.gui.controller.solver;

import com.gitlab.super7ramp.crosswords.api.solver.SolverUsecase;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordSolverViewModel;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

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
        solverService.setOnSucceeded(e -> LOGGER.info("Solving succeeded"));
        solverService.setOnRunning(e -> LOGGER.info("Solving in progress"));
        solverService.setOnFailed(e -> LOGGER.info("Solving failed"));
        crosswordSolverViewModel.solverRunning().bind(solverService.runningProperty());
    }

    /**
     * Starts the solver.
     */
    public void start() {
        stop();
        solverService.start();
    }

    /**
     * Stops the solver.
     */
    public void stop() {
        solverService.cancel();
        solverService.reset();
    }

}
