/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.controller.puzzle;

import javafx.concurrent.Task;
import re.belv.croiseur.api.puzzle.persistence.PuzzlePersistenceService;
import re.belv.croiseur.gui.view.model.PuzzleSelectionViewModel;

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
    LoadSelectedPuzzleTask(
            final PuzzleSelectionViewModel puzzleSelectionViewModelArg,
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
