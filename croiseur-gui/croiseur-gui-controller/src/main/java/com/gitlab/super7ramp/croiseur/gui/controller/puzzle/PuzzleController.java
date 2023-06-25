/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import javafx.concurrent.Task;

import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controls calls to the puzzle service.
 */
public final class PuzzleController {

    /**
     * The 'list puzzles' task.
     */
    private static final class ListPuzzleTask extends Task<Void> {

        private static final Logger LOGGER = Logger.getLogger(ListPuzzleTask.class.getName());

        private final PuzzleService puzzleService;

        ListPuzzleTask(final PuzzleService puzzleServiceArg) {
            puzzleService = puzzleServiceArg;
            setOnFailed(
                    event -> LOGGER.log(Level.WARNING, "List Puzzle task failed.", getException()));
        }

        @Override
        protected Void call() {
            puzzleService.list();
            return null;
        }
    }

    /** The worker executing puzzle tasks. */
    private final Executor executor;

    /** The task to execute for listing puzzles. */
    private final Task<Void> listPuzzlesTask;

    /**
     * Constructs an instance.
     *
     * @param puzzleServiceArg the puzzle service
     * @param executorArg      the worker executing the puzzle tasks
     */
    public PuzzleController(final PuzzleService puzzleServiceArg, final Executor executorArg) {
        listPuzzlesTask = new ListPuzzleTask(puzzleServiceArg);
        executor = executorArg;
    }

    /**
     * Starts the 'list puzzles' task.
     */
    public void listAvailablePuzzles() {
        executor.execute(listPuzzlesTask);
    }
}
