/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.common.puzzle.ChangedPuzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordGridViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleSelectionViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleViewModel;
import javafx.concurrent.Task;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Save (current) puzzle task.
 */
final class SavePuzzleTask extends Task<Void> {

    /** The puzzle service. */
    private final PuzzleService puzzleService;

    /** The current puzzle grid, to be saved. */
    private final PuzzleGrid currentGrid;

    /** The last saved puzzle. {@code null} if puzzle hasn't been saved yet. */
    private final SavedPuzzle lastSavedPuzzle;

    /**
     * Constructs an instance.
     *
     * @param puzzleServiceArg            the puzzle service
     * @param puzzleSelectionViewModelArg the puzzle selection view model
     * @param crosswordGridViewModelArg   the crossword grid view model
     */
    SavePuzzleTask(final PuzzleService puzzleServiceArg,
                   final PuzzleSelectionViewModel puzzleSelectionViewModelArg,
                   final CrosswordGridViewModel crosswordGridViewModelArg) {
        puzzleService = puzzleServiceArg;
        lastSavedPuzzle = convertToSavedPuzzle(puzzleSelectionViewModelArg.selectedPuzzle());
        currentGrid = convertToPuzzleGrid(crosswordGridViewModelArg);
    }

    @Override
    protected Void call() {
        if (lastSavedPuzzle == null) {
            create();
        } else {
            update();
        }
        return null;
    }

    /**
     * Converts puzzle view model to domain puzzle type.
     *
     * @param puzzleViewModel the puzzle view model
     * @return the puzzle converted to domain puzzle type; {@code null} if given view model is
     * {@code null}
     */
    private static SavedPuzzle convertToSavedPuzzle(final PuzzleViewModel puzzleViewModel) {
        if (puzzleViewModel == null) {
            return null;
        }
        final long id = puzzleViewModel.id();
        final int revision = puzzleViewModel.revision();
        final PuzzleDetails details =
                new PuzzleDetails(puzzleViewModel.title(), puzzleViewModel.author(),
                                  puzzleViewModel.editor(), puzzleViewModel.copyright(),
                                  puzzleViewModel.date().isEmpty() ? Optional.empty() :
                                          Optional.of(LocalDate.parse(puzzleViewModel.date())));
        final PuzzleGrid grid = puzzleViewModel.grid();
        final Puzzle data = new Puzzle(details, grid);
        return new SavedPuzzle(id, data, revision);
    }

    /**
     * Creates a {@link PuzzleGrid} from the current crossword grid view model.
     *
     * @return a {@link PuzzleGrid} corresponding to the current crossword grid view model
     */
    private static PuzzleGrid convertToPuzzleGrid(
            final CrosswordGridViewModel crosswordGridViewModel) {
        final PuzzleGrid.Builder builder = new PuzzleGrid.Builder();
        builder.height(crosswordGridViewModel.rowCount());
        builder.width(crosswordGridViewModel.columnCount());
        crosswordGridViewModel.boxesProperty().forEach((position, box) -> {
            if (box.isShaded()) {
                builder.shade(position);
            } else {
                final String content = box.userContent();
                if (!content.isEmpty()) {
                    builder.fill(position, content.charAt(0));
                }
            }
        });
        return builder.build();
    }

    /** Saves the puzzle for the first time. */
    private void create() {
        final String author = System.getProperty("user.name");
        final LocalDate date = LocalDate.now();
        final PuzzleDetails details = new PuzzleDetails("", author, "", "", Optional.of(date));
        final Puzzle puzzle = new Puzzle(details, currentGrid);
        puzzleService.save(puzzle);
    }

    /** Updates an already saved puzzle. */
    private void update() {
        final ChangedPuzzle changedPuzzle = lastSavedPuzzle.modifiedWith(currentGrid);
        puzzleService.save(changedPuzzle);
    }

}
