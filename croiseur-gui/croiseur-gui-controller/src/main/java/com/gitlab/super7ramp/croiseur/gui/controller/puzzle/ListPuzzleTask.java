/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import javafx.concurrent.Task;

/**
 * List puzzle task.
 */
final class ListPuzzleTask extends Task<Void> {

    /** The puzzle service. */
    private final PuzzleService puzzleService;

    /**
     * Constructs an instance.
     *
     * @param puzzleServiceArg the puzzle service
     */
    ListPuzzleTask(final PuzzleService puzzleServiceArg) {
        puzzleService = puzzleServiceArg;
    }

    @Override
    protected Void call() {
        puzzleService.list();
        return null;
    }
}