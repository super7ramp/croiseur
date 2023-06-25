/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.presenter;

import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.gui.view.model.ErrorsViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleSelectionViewModel;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import javafx.application.Platform;

import java.util.List;
import java.util.logging.Logger;

/**
 * GUI implementation of {@link PuzzlePresenter}.
 */
final class GuiPuzzlePresenter implements PuzzlePresenter {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(GuiPuzzlePresenter.class.getName());

    /** The puzzle selection view model. */
    private final PuzzleSelectionViewModel puzzleSelectionViewModel;

    /** The errors view model. */
    private final ErrorsViewModel errorsViewModel;

    /**
     * Constructs an instance.
     *
     * @param puzzleSelectionViewModelArg the puzzle selection view model
     * @param errorsViewModelArg the errors view model
     */
    GuiPuzzlePresenter(final PuzzleSelectionViewModel puzzleSelectionViewModelArg,
                       final ErrorsViewModel errorsViewModelArg) {
        puzzleSelectionViewModel = puzzleSelectionViewModelArg;
        errorsViewModel = errorsViewModelArg;
    }

    @Override
    public void presentAvailablePuzzles(final List<SavedPuzzle> puzzles) {
        LOGGER.info(() -> "Received available puzzles: " + puzzles);
        Platform.runLater(() -> puzzleSelectionViewModel.availablePuzzlesProperty().setAll(puzzles));
    }

    @Override
    public void presentLoadedPuzzle(final SavedPuzzle puzzle) {
        // Not used by GUI.
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void presentPuzzleRepositoryError(final String error) {
        LOGGER.warning(() -> "Received puzzle repository error: " + error);
        Platform.runLater(() -> errorsViewModel.addError(error));
    }

    @Override
    public void presentSavedPuzzle(final SavedPuzzle puzzle) {
        // TODO presents a kind of confirmation of the saving
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
