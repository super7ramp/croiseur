package com.gitlab.super7ramp.crosswords.gui.controller.solver;

import com.gitlab.super7ramp.crosswords.api.solver.SolverUsecase;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.SolverViewModel;
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
     * @param solverViewModel the solver view model
     * @param usecase         the "solve crossword" usecase
     */
    public SolverController(final SolverViewModel solverViewModel, final SolverUsecase usecase) {
        solverService = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new SolveTask(solverViewModel.crosswordViewModel(),
                        solverViewModel.dictionaryViewModel(), usecase);
            }
        };
        solverService.setOnSucceeded(e -> LOGGER.info("Solving succeeded"));
        solverService.setOnRunning(e -> LOGGER.info("Solving in progress"));
        solverService.setOnFailed(e -> LOGGER.info("Solving failed"));
        solverViewModel.solverRunning().bind(solverService.runningProperty());
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
