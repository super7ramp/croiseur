package com.gitlab.super7ramp.crosswords.gui.controller.solver;

import com.gitlab.super7ramp.crosswords.api.solver.SolveRequest;
import com.gitlab.super7ramp.crosswords.api.solver.SolverService;
import com.gitlab.super7ramp.crosswords.gui.view.model.CrosswordGridViewModel;
import com.gitlab.super7ramp.crosswords.gui.view.model.DictionariesViewModel;
import javafx.concurrent.Task;

/**
 * Solve task.
 */
final class SolveTask extends Task<Void> {

    /** The "solve crossword" usecase. */
    private final SolverService solverService;

    /** The solve request. */
    private final SolveRequest solveRequest;

    /**
     * Constructs an instance.
     *
     * @param crosswordGridViewModelArg the crossword model
     * @param dictionariesViewModelArg    the dictionary model
     * @param solverServiceArg          the "solve crossword" usecase
     */
    SolveTask(final CrosswordGridViewModel crosswordGridViewModelArg,
              final DictionariesViewModel dictionariesViewModelArg,
              final SolverService solverServiceArg) {
        solveRequest = new SolveRequestImpl(crosswordGridViewModelArg, dictionariesViewModelArg);
        solverService = solverServiceArg;
    }

    @Override
    protected Void call() {
        solverService.solve(solveRequest);
        return null;
    }

}
