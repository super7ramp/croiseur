/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package com.gitlab.super7ramp.croiseur.gui.controller.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.importer.PuzzleImportService;
import javafx.concurrent.Task;

/**
 * The 'list puzzle decoders' task.
 */
final class ListPuzzleDecodersTask extends Task<Void> {

    /** The puzzle service to call. */
    private final PuzzleImportService puzzleImportService;

    /**
     * Constructs an instance.
     *
     * @param puzzleImportServiceArg the puzzle import service
     */
    ListPuzzleDecodersTask(final PuzzleImportService puzzleImportServiceArg) {
        puzzleImportService = puzzleImportServiceArg;
    }

    @Override
    protected Void call() {
        puzzleImportService.listDecoders();
        return null;
    }
}
