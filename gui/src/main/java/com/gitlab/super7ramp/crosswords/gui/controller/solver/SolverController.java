package com.gitlab.super7ramp.crosswords.gui.controller.solver;

import com.gitlab.super7ramp.crosswords.api.solver.SolverUsecase;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordViewModel;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.DictionaryViewModel;
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
     * @param crosswordViewModel  the crossword model
     * @param dictionaryViewModel the dictionary model
     * @param usecase             the "solve crossword" usecase
     */
    public SolverController(final CrosswordViewModel crosswordViewModel,
                            final DictionaryViewModel dictionaryViewModel,
                            final SolverUsecase usecase) {
        solverService = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new SolveTask(crosswordViewModel, dictionaryViewModel, usecase);
            }
        };
        solverService.setOnSucceeded(e -> LOGGER.info("Solving succeeded"));
        solverService.setOnRunning(e -> LOGGER.info("Solving in progress"));
        solverService.setOnFailed(e -> LOGGER.info("Solving failed"));
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
