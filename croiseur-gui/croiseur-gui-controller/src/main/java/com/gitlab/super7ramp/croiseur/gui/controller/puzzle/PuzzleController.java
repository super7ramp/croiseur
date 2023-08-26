/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.gui.view.model.puzzle.edition.PuzzleEditionViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.puzzle.selection.PuzzleSelectionViewModel;
import javafx.concurrent.Task;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controls calls to the puzzle service.
 */
public final class PuzzleController {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(PuzzleController.class.getName());

    /** The puzzle selection view model. */
    private final PuzzleSelectionViewModel puzzleSelectionViewModel;

    /** The puzzle edition view model. */
    private final PuzzleEditionViewModel puzzleEditionViewModel;

    /** The puzzle service to call. */
    private final PuzzleService puzzleService;

    /** The worker executing puzzle tasks. */
    private final Executor executor;

    /**
     * Constructs an instance.
     *
     * @param puzzleSelectionViewModelArg the puzzle selection view model
     * @param puzzleEditionViewModelArg   the puzzle edition view model
     * @param puzzleServiceArg            the puzzle service
     * @param executorArg                 the worker executing the puzzle tasks
     */
    public PuzzleController(final PuzzleSelectionViewModel puzzleSelectionViewModelArg,
                            final PuzzleEditionViewModel puzzleEditionViewModelArg,
                            final PuzzleService puzzleServiceArg, final Executor executorArg) {
        puzzleSelectionViewModel = puzzleSelectionViewModelArg;
        puzzleEditionViewModel = puzzleEditionViewModelArg;
        puzzleService = puzzleServiceArg;
        executor = executorArg;
    }

    /**
     * Starts the 'list puzzles' task.
     */
    public void listPuzzles() {
        final var task = new ListPuzzlesTask(puzzleService.persistence());
        execute(task);
    }

    /**
     * Starts the 'list puzzle decoders' task.
     */
    public void listPuzzleDecoders() {
        final var task = new ListPuzzleDecodersTask(puzzleService.importer());
        execute(task);
    }

    /**
     * Starts the 'list puzzle encoders' task.
     */
    public void listPuzzleEncoders() {
        final var task = new ListPuzzleEncodersTask(puzzleService.exporter());
        execute(task);
    }

    /**
     * Starts the 'load selected puzzle' task.
     */
    public void loadSelectedPuzzle() {
        // It is artificial because we have all information to load the grid now but let's prevent
        // the controller to modify the view model directly.
        final var task =
                new LoadSelectedPuzzleTask(puzzleSelectionViewModel, puzzleService.persistence());
        execute(task);
    }

    /**
     * Starts the 'delete puzzle' task.
     */
    public void deleteSelectedPuzzle() {
        final var task =
                new DeleteSelectedPuzzleTask(puzzleSelectionViewModel, puzzleService.persistence());
        execute(task);
    }

    /**
     * Starts the 'save puzzle' task.
     */
    public void savePuzzle() {
        final var task = new SavePuzzleTask(puzzleEditionViewModel, puzzleService.persistence());
        execute(task);
    }

    /**
     * Starts the 'import puzzle' task.
     *
     * @param selectedFile   the file to import
     * @param selectedFormat the puzzle format of the file to import
     */
    public void importPuzzle(final File selectedFile, final String selectedFormat) {
        final var task =
                new ImportPuzzleTask(selectedFile, selectedFormat, puzzleService.importer());
        execute(task);
    }

    /**
     * Starts the 'export puzzle' task.
     *
     * @param selectedFile   the destination file
     * @param selectedFormat the puzzle format of the export
     */
    public void exportPuzzle(final File selectedFile, final String selectedFormat) {
        final var task =
                new ExportPuzzleTask(puzzleEditionViewModel, selectedFile, selectedFormat,
                                     puzzleService.exporter());
        execute(task);
    }

    /**
     * Executes the given task.
     *
     * @param task the task to execute
     */
    private void execute(final Task<Void> task) {
        task.setOnFailed(event -> LOGGER.log(Level.WARNING, "Puzzle task failed.",
                                             task.getException()));
        executor.execute(task);
    }
}
