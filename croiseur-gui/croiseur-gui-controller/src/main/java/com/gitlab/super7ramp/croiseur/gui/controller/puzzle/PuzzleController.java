/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
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

    /** The worker executing puzzle tasks. */
    private final Executor executor;

    /** The puzzle service to call. */
    private final PuzzleService puzzleService;

    /** The puzzle selection view model. */
    private final PuzzleSelectionViewModel puzzleSelectionViewModel;

    /**
     * Constructs an instance.
     *
     * @param puzzleSelectionViewModelArg the puzzle selection view model
     * @param puzzleServiceArg            the puzzle service
     * @param executorArg                 the worker executing the puzzle tasks
     */
    public PuzzleController(final PuzzleSelectionViewModel puzzleSelectionViewModelArg,
                            final PuzzleService puzzleServiceArg,
                            final Executor executorArg) {
        puzzleSelectionViewModel = puzzleSelectionViewModelArg;
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

    /** Starts the 'load selected puzzle' task. */
    public void loadSelectedPuzzle() {
        // Yes, it is artificial because we have all information to load the grid but let's avoid
        // controller to modify the view model directly.
        final var task = new LoadSelectedPuzzleTask(puzzleService, puzzleSelectionViewModel);
        execute(task);
    }

    /**
     * Executes the given task.
     *
     * @param task the task to execute
     */
    private void execute(final Task<Void> task) {
        task.setOnFailed(event -> LOGGER.log(Level.WARNING, "Dictionary task failed.",
                                             task.getException()));
        executor.execute(task);
    }
}
