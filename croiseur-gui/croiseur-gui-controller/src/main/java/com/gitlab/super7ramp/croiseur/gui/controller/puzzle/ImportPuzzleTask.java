/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.importer.PuzzleImportService;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * The 'import puzzle' task.
 */
final class ImportPuzzleTask extends Task<Void> {

    /** The file to import. */
    private final File selectedFile;

    /** The selected puzzle format. */
    private final String selectedFormat;

    /** The puzzle service to call. */
    private final PuzzleImportService puzzleImportService;

    /**
     * Constructs an instance.
     *
     * @param selectedFileArg        the file to import
     * @param selectedFormatArg      the selected puzzle format
     * @param puzzleImportServiceArg the puzzle service to call
     */
    ImportPuzzleTask(final File selectedFileArg, final String selectedFormatArg,
                     final PuzzleImportService puzzleImportServiceArg) {
        selectedFile = selectedFileArg;
        selectedFormat = selectedFormatArg;
        puzzleImportService = puzzleImportServiceArg;
    }

    @Override
    protected Void call() throws IOException {
        try (final var selectedPuzzleInputStream = new FileInputStream(selectedFile)) {
            puzzleImportService.importPuzzle(selectedFormat, selectedPuzzleInputStream);
        }
        return null;
    }

}
