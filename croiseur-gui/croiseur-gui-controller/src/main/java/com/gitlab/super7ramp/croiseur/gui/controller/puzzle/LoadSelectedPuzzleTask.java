/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleSelectionViewModel;
import javafx.concurrent.Task;

/**
 * Load selected puzzle task.
 */
final class LoadSelectedPuzzleTask extends Task<Void> {

    /** The puzzle service. */
    private final PuzzleService puzzleService;

    /** The selected puzzle id. */
    private final long selectedPuzzleId;

    /**
     * Constructs an instance.
     *
     * @param puzzleSelectionViewModelArg the puzzle selection view model
     * @param puzzleServiceArg            the puzzle service
     */
    LoadSelectedPuzzleTask(final PuzzleSelectionViewModel puzzleSelectionViewModelArg,
                           final PuzzleService puzzleServiceArg) {
        puzzleService = puzzleServiceArg;
        selectedPuzzleId = puzzleSelectionViewModelArg.selectedPuzzle().id();
    }

    @Override
    protected Void call() {
        puzzleService.load(selectedPuzzleId);
        return null;
    }
}
