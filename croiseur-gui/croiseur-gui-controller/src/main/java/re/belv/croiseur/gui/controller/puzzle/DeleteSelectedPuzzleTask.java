/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.controller.puzzle;

import javafx.concurrent.Task;
import re.belv.croiseur.api.puzzle.persistence.PuzzlePersistenceService;
import re.belv.croiseur.gui.view.model.PuzzleSelectionViewModel;

/** Delete selected puzzle task. */
final class DeleteSelectedPuzzleTask extends Task<Void> {

    /** The selected puzzle id. */
    private final long puzzleId;

    /** The puzzle service to call. */
    private final PuzzlePersistenceService puzzlePersistenceService;

    /**
     * Constructs an instance.
     *
     * @param puzzleSelectionViewModel the puzzle selection view model
     * @param puzzlePersistenceServiceArg the puzzle service
     */
    public DeleteSelectedPuzzleTask(
            final PuzzleSelectionViewModel puzzleSelectionViewModel,
            final PuzzlePersistenceService puzzlePersistenceServiceArg) {
        puzzleId = puzzleSelectionViewModel.selectedPuzzle().id();
        puzzlePersistenceService = puzzlePersistenceServiceArg;
    }

    @Override
    protected Void call() {
        puzzlePersistenceService.delete(puzzleId);
        return null;
    }
}
