/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package re.belv.croiseur.gui.controller.puzzle;

import javafx.concurrent.Task;
import re.belv.croiseur.api.puzzle.exporter.PuzzleExportService;

/**
 * The 'list puzzle encoders' task.
 */
final class ListPuzzleEncodersTask extends Task<Void> {

    /** The puzzle service to call. */
    private final PuzzleExportService puzzleExportService;

    /**
     * Constructs an instance.
     *
     * @param puzzleExportServiceArg the puzzle export service
     */
    ListPuzzleEncodersTask(final PuzzleExportService puzzleExportServiceArg) {
        puzzleExportService = puzzleExportServiceArg;
    }

    @Override
    protected Void call() {
        puzzleExportService.listEncoders();
        return null;
    }
}
