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
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleDetailsViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleEditionViewModel;
import javafx.concurrent.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;

/**
 * Save (current) puzzle task.
 */
final class SavePuzzleTask extends Task<Void> {

    /** The date formatter - assuming constant locale during application run-time. */
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

    /** The actual task. */
    private final Runnable task;

    /**
     * Constructs an instance.
     *
     * @param puzzleEditionViewModel the puzzle edition view model
     * @param puzzleService          the puzzle service
     */
    SavePuzzleTask(final PuzzleEditionViewModel puzzleEditionViewModel,
                   final PuzzleService puzzleService) {
        task = buildTask(puzzleEditionViewModel, puzzleService);
        puzzleEditionViewModel.savingInProgressProperty().bind(runningProperty());
    }

    /**
     * Builds the actual task - either a creation or an update.
     *
     * @param puzzleEditionViewModel the puzzle edition view model
     * @param puzzleService          the puzzle service
     * @return the actual task
     */
    private static Runnable buildTask(final PuzzleEditionViewModel puzzleEditionViewModel,
                                      final PuzzleService puzzleService) {
        final Runnable task;
        if (puzzleEditionViewModel.puzzleDetailsViewModel().id() != 0L) {
            task = updateTaskFrom(puzzleEditionViewModel, puzzleService);
        } else {
            task = createTaskFrom(puzzleEditionViewModel, puzzleService);
        }
        return task;
    }

    /**
     * Returns a new task suitable to saves the puzzle for the first time.
     *
     * @param puzzleEditionViewModel the puzzle edition view model
     * @param puzzleService          the puzzle service
     * @return a new task suitable to saves the puzzle for the first time
     */
    private static Runnable createTaskFrom(final PuzzleEditionViewModel puzzleEditionViewModel,
                                           final PuzzleService puzzleService) {
        final PuzzleDetails details =
                convertToDomain(puzzleEditionViewModel.puzzleDetailsViewModel());
        final PuzzleGrid grid = convertToDomain(puzzleEditionViewModel.crosswordGridViewModel());
        final Puzzle puzzle = new Puzzle(details, grid);
        return () -> puzzleService.save(puzzle);
    }

    /**
     * Returns a new task suitable to Updates an already saved puzzle.
     *
     * @param puzzleEditionViewModel the puzzle edition view model
     * @param puzzleService          the puzzle service
     * @return a new task suitable to Updates an already saved puzzle.
     */
    private static Runnable updateTaskFrom(final PuzzleEditionViewModel puzzleEditionViewModel,
                                           final PuzzleService puzzleService) {

        final PuzzleDetailsViewModel puzzleDetailsViewModel =
                puzzleEditionViewModel.puzzleDetailsViewModel();
        final PuzzleDetails details = convertToDomain(puzzleDetailsViewModel);
        final long id = puzzleDetailsViewModel.id();
        final int revision = puzzleDetailsViewModel.revision();
        final PuzzleGrid currentGrid =
                convertToDomain(puzzleEditionViewModel.crosswordGridViewModel());
        final Puzzle puzzle = new Puzzle(details, currentGrid);
        final ChangedPuzzle changedPuzzle = new SavedPuzzle(id, puzzle, revision).asChangedPuzzle();

        return () -> puzzleService.save(changedPuzzle);
    }

    /**
     * Converts details view model to domain type.
     *
     * @param puzzleDetailsViewModel the puzzle details view model
     * @return the puzzle details converted to domain type
     */
    private static PuzzleDetails convertToDomain(
            final PuzzleDetailsViewModel puzzleDetailsViewModel) {
        final Optional<LocalDate> optDate;
        final String viewModelDate = puzzleDetailsViewModel.date();
        if (viewModelDate.isEmpty()) {
            optDate = Optional.empty();
        } else {
            final LocalDate date = LocalDate.parse(viewModelDate, DATE_FORMATTER);
            optDate = Optional.of(date);
        }
        return new PuzzleDetails(puzzleDetailsViewModel.title(), puzzleDetailsViewModel.author(),
                                 puzzleDetailsViewModel.editor(),
                                 puzzleDetailsViewModel.copyright(), optDate);
    }

    /**
     * Converts crossword grid view model to domain puzzle type.
     *
     * @return the grid converted to domain type
     */
    private static PuzzleGrid convertToDomain(final CrosswordGridViewModel crosswordGridViewModel) {
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

    @Override
    protected Void call() {
        task.run();
        return null;
    }
}
