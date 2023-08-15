/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.clue;

import com.gitlab.super7ramp.croiseur.api.clue.ClueService;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordGridViewModel;
import javafx.concurrent.Task;

import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controls calls to the clue service.
 */
public final class ClueController {

    /** The logger. */
    private static final Logger LOGGER = Logger.getLogger(ClueController.class.getName());

    /** The clues view model. */
    private final CrosswordGridViewModel crosswordGridViewModel;

    /** The clue service to call. */
    private final ClueService clueService;

    /** The worker executing the clue tasks. */
    private final Executor executor;

    /**
     * Constructs an instance.
     *
     * @param crosswordGridViewModelArg the crossword grid view model
     * @param clueServiceArg            the clue service to call
     * @param executorArg               the worker executing the clue tasks.
     */
    public ClueController(final CrosswordGridViewModel crosswordGridViewModelArg,
                          final ClueService clueServiceArg,
                          final Executor executorArg) {
        crosswordGridViewModel = crosswordGridViewModelArg;
        clueService = clueServiceArg;
        executor = executorArg;
    }

    /**
     * Gets the clue for the currently selected slot.
     */
    public void getClueForCurrentSlot() {
        execute(new GetClueTask(crosswordGridViewModel, clueService));
    }

    /**
     * Executes the given task.
     *
     * @param task the task to execute
     */
    private void execute(final Task<Void> task) {
        task.setOnFailed(
                event -> LOGGER.log(Level.WARNING, "Clue task failed.", task.getException()));
        executor.execute(task);
    }
}
