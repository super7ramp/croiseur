/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.presenter;

import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordBoxViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordGridViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.ErrorsViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleSelectionViewModel;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import javafx.application.Platform;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * GUI implementation of {@link PuzzlePresenter}.
 */
final class GuiPuzzlePresenter implements PuzzlePresenter {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(GuiPuzzlePresenter.class.getName());

    /** The puzzle selection view model. */
    private final PuzzleSelectionViewModel puzzleSelectionViewModel;

    /** The crossword grid view model. */
    private final CrosswordGridViewModel crosswordGridViewModel;

    /** The errors view model. */
    private final ErrorsViewModel errorsViewModel;

    /**
     * Constructs an instance.
     *
     * @param puzzleSelectionViewModelArg the puzzle selection view model
     * @param crosswordGridViewModelArg   the crossword grid view model
     * @param errorsViewModelArg          the errors view model
     */
    GuiPuzzlePresenter(
            final PuzzleSelectionViewModel puzzleSelectionViewModelArg,
            final CrosswordGridViewModel crosswordGridViewModelArg,
            final ErrorsViewModel errorsViewModelArg) {
        puzzleSelectionViewModel = puzzleSelectionViewModelArg;
        crosswordGridViewModel = crosswordGridViewModelArg;
        errorsViewModel = errorsViewModelArg;
    }

    @Override
    public void presentAvailablePuzzles(final List<SavedPuzzle> puzzles) {
        LOGGER.info(() -> "Received available puzzles: " + puzzles);
        Platform.runLater(
                () -> puzzleSelectionViewModel.availablePuzzlesProperty().setAll(puzzles));
    }

    @Override
    public void presentLoadedPuzzle(final SavedPuzzle puzzle) {
        final PuzzleGrid grid = puzzle.grid();
        Platform.runLater(() -> fillGridViewModelWith(grid));
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

    /**
     * Synchronizes the grid view model with given puzzle grid.
     *
     * @param grid the puzzle grid
     */
    private void fillGridViewModelWith(final PuzzleGrid grid) {
        crosswordGridViewModel.resizeTo(grid.width(), grid.height());

        final Set<GridPosition> positionsToUpdate =
                new HashSet<>(crosswordGridViewModel.boxesProperty().keySet());

        grid.filled().forEach((position, letter) -> {
            positionsToUpdate.remove(position);
            final CrosswordBoxViewModel box = crosswordGridViewModel.box(position);
            box.lighten();
            box.userContent(String.valueOf(letter));
        });

        grid.shaded().forEach(position -> {
            positionsToUpdate.remove(position);
            crosswordGridViewModel.box(position).shade();
        });

        positionsToUpdate.forEach(position -> {
            final CrosswordBoxViewModel box = crosswordGridViewModel.box(position);
            box.lighten();
            box.userContent("");
        });
    }

}
