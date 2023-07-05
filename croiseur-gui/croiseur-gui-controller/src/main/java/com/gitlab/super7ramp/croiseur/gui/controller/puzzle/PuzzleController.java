/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleEditionViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleSelectionViewModel;
import javafx.concurrent.Task;

import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controls calls to the puzzle service.
 */
public final class PuzzleController {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(PuzzleController.class.getName());

    /** The puzzle selection view model. */
    private final PuzzleSelectionViewModel puzzleSelectionViewModel;

    /** The puzzle edition view model. */
    private final PuzzleEditionViewModel puzzleEditionViewModel;

    /** The puzzle service to call. */
    private final PuzzleService puzzleService;

    /** The worker executing puzzle tasks. */
    private final Executor executor;

    /**
     * Constructs an instance.
     *
     * @param puzzleSelectionViewModelArg the puzzle selection view model
     * @param puzzleEditionViewModelArg   the puzzle edition view model
     * @param puzzleServiceArg            the puzzle service
     * @param executorArg                 the worker executing the puzzle tasks
     */
    public PuzzleController(final PuzzleSelectionViewModel puzzleSelectionViewModelArg,
                            final PuzzleEditionViewModel puzzleEditionViewModelArg,
                            final PuzzleService puzzleServiceArg, final Executor executorArg) {
        puzzleSelectionViewModel = puzzleSelectionViewModelArg;
        puzzleEditionViewModel = puzzleEditionViewModelArg;
        puzzleService = puzzleServiceArg;
        executor = executorArg;
    }

    /**
     * Starts the 'list puzzles' task.
     */
    public void listAvailablePuzzles() {
        final var task = new ListPuzzleTask(puzzleService);
        execute(task);
    }

    /**
     * Starts the 'load selected puzzle' task.
     */
    public void loadSelectedPuzzle() {
        // It is artificial because we have all information to load the grid now but let's prevent
        // the controller to modify the view model directly.
        final var task = new LoadSelectedPuzzleTask(puzzleSelectionViewModel, puzzleService);
        execute(task);
    }

    /**
     * Starts the 'save puzzle' task.
     */
    public void savePuzzle() {
        final var task = new SavePuzzleTask(puzzleEditionViewModel, puzzleService);
        execute(task);
    }

    /**
     * Executes the given task.
     *
     * @param task the task to execute
     */
    private void execute(final Task<Void> task) {
        task.setOnFailed(event -> LOGGER.log(Level.WARNING, "Puzzle task failed.",
                                             task.getException()));
        executor.execute(task);
    }
}
