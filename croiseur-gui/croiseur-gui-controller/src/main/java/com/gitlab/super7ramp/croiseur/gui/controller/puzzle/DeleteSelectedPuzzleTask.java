/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleSelectionViewModel;
import javafx.concurrent.Task;

/**
 * Delete selected puzzle task.
 */
final class DeleteSelectedPuzzleTask extends Task<Void> {

    /** The selected puzzle id. */
    private final long puzzleId;

    /** The puzzle service to call. */
    private final PuzzleService puzzleService;

    /**
     * Constructs an instance.
     *
     * @param puzzleSelectionViewModel the puzzle selection view model
     * @param puzzleServiceArg         the puzzle service
     */
    public DeleteSelectedPuzzleTask(final PuzzleSelectionViewModel puzzleSelectionViewModel,
                                    final PuzzleService puzzleServiceArg) {
        puzzleId = puzzleSelectionViewModel.selectedPuzzle().id();
        puzzleService = puzzleServiceArg;
    }

    @Override
    protected Void call() {
        puzzleService.delete(puzzleId);
        return null;
    }
}
