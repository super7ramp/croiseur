/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.persistence.PuzzlePersistenceService;
import com.gitlab.super7ramp.croiseur.gui.view.model.puzzle.selection.PuzzleSelectionViewModel;
import javafx.concurrent.Task;

/**
 * Load selected puzzle task.
 */
final class LoadSelectedPuzzleTask extends Task<Void> {

    /** The puzzle service. */
    private final PuzzlePersistenceService puzzlePersistenceService;

    /** The selected puzzle id. */
    private final long selectedPuzzleId;

    /**
     * Constructs an instance.
     *
     * @param puzzleSelectionViewModelArg the puzzle selection view model
     * @param puzzlePersistenceServiceArg            the puzzle service
     */
    LoadSelectedPuzzleTask(final PuzzleSelectionViewModel puzzleSelectionViewModelArg,
                           final PuzzlePersistenceService puzzlePersistenceServiceArg) {
        puzzlePersistenceService = puzzlePersistenceServiceArg;
        selectedPuzzleId = puzzleSelectionViewModelArg.selectedPuzzle().id();
    }

    @Override
    protected Void call() {
        puzzlePersistenceService.load(selectedPuzzleId);
        return null;
    }
}
