/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.solver;

import com.gitlab.super7ramp.croiseur.api.solver.SolveRequest;
import com.gitlab.super7ramp.croiseur.api.solver.SolverService;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordGridViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.DictionariesViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.SolverConfigurationViewModel;
import javafx.concurrent.Task;

import java.util.Random;

/**
 * Solve task.
 */
final class SolveTask extends Task<Void> {

    /** The solver service */
    private final SolverService solverService;

    /** The solve request. */
    private final SolveRequest solveRequest;

    /**
     * Constructs an instance.
     *
     * @param crosswordGridViewModelArg       the crossword model
     * @param dictionariesViewModelArg        the dictionary model
     * @param solverConfigurationViewModelArg the solver configuration view mode
     * @param solverServiceArg                the solver service
     * @param randomArg                       the source of randomness
     */
    SolveTask(final CrosswordGridViewModel crosswordGridViewModelArg,
              final DictionariesViewModel dictionariesViewModelArg,
              final SolverConfigurationViewModel solverConfigurationViewModelArg,
              final SolverService solverServiceArg, final Random randomArg) {
        solveRequest = new SolveRequestImpl(crosswordGridViewModelArg, dictionariesViewModelArg,
                                            solverConfigurationViewModelArg, randomArg);
        solverService = solverServiceArg;
    }

    @Override
    protected Void call() {
        solverService.solve(solveRequest);
        return null;
    }

}
