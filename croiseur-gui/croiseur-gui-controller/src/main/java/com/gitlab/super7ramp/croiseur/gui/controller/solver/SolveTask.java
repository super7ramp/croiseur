/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.solver;

import com.gitlab.super7ramp.croiseur.api.solver.SolveRequest;
import com.gitlab.super7ramp.croiseur.api.solver.SolverService;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordGridViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.DictionariesViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.SolverSelectionViewModel;
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
     * @param dictionariesViewModelArg  the dictionary model
     * @param solverServiceArg          the "solve crossword" usecase
     */
    SolveTask(final CrosswordGridViewModel crosswordGridViewModelArg,
              final DictionariesViewModel dictionariesViewModelArg,
              final SolverSelectionViewModel solverSelectionViewModelArg,
              final SolverService solverServiceArg) {
        solveRequest = new SolveRequestImpl(crosswordGridViewModelArg, dictionariesViewModelArg,
                solverSelectionViewModelArg);
        solverService = solverServiceArg;
    }

    @Override
    protected Void call() {
        solverService.solve(solveRequest);
        return null;
    }

}
