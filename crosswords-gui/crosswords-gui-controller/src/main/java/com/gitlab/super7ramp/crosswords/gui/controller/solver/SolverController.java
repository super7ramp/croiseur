package com.gitlab.super7ramp.crosswords.gui.controller.solver;

import com.gitlab.super7ramp.crosswords.api.solver.SolverUsecase;
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
                return new SolveTask(crosswordSolverViewModel.crosswordGridViewModel(),
                        crosswordSolverViewModel.dictionaryViewModel(), usecase);
            }
        };
        solverService.setOnReady(e -> LOGGER.info("Solver ready"));
        solverService.setOnRunning(e -> LOGGER.info("Solving"));
        solverService.setOnCancelled(e -> LOGGER.info("Solver cancelled"));
        solverService.setOnSucceeded(e -> LOGGER.info("Solver finished"));
        solverService.setOnFailed(e -> LOGGER.log(Level.WARNING, "Solver failed",
                e.getSource().getException()));
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
     * Stops the solver.
     */
    public void stop() {
        solverService.cancel();
        solverService.reset();
    }

}
