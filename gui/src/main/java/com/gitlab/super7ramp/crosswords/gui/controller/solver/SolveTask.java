package com.gitlab.super7ramp.crosswords.gui.controller.solver;

import com.gitlab.super7ramp.crosswords.api.solver.SolveRequest;
import com.gitlab.super7ramp.crosswords.api.solver.SolverUsecase;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordViewModel;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.DictionaryViewModel;
import javafx.concurrent.Task;

/**
 * Solve task.
 */
final class SolveTask extends Task<Void> {

    /** The "solve crossword" usecase. */
    private final SolverUsecase solverUsecase;

    /** The solve request. */
    private final SolveRequest solveRequest;

    /**
     * Constructs an instance.
     *
     * @param crosswordViewModelArg  the crossword model
     * @param dictionaryViewModelArg the dictionary model
     * @param solverUsecaseArg       the "solve crossword" usecase
     */
    SolveTask(final CrosswordViewModel crosswordViewModelArg,
              final DictionaryViewModel dictionaryViewModelArg,
              final SolverUsecase solverUsecaseArg) {
        solveRequest = new SolveRequestImpl(crosswordViewModelArg, dictionaryViewModelArg);
        solverUsecase = solverUsecaseArg;
    }

    @Override
    protected Void call() {
        solverUsecase.solve(solveRequest);
        return null;
    }

}
