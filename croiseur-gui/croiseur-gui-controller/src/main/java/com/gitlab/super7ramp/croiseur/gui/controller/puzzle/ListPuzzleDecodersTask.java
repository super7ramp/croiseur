/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import javafx.concurrent.Task;

/**
 * The 'list puzzle decoders' task.
 */
final class ListPuzzleDecodersTask extends Task<Void> {

    /** The puzzle service to call. */
    private final PuzzleService puzzleService;

    /**
     * Constructs an instance.
     *
     * @param puzzleServiceArg the puzzle service
     */
    ListPuzzleDecodersTask(final PuzzleService puzzleServiceArg) {
        puzzleService = puzzleServiceArg;
    }

    @Override
    protected Void call() {
        puzzleService.listDecoders();
        return null;
    }
}
