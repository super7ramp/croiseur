/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.persistence.PuzzlePersistenceService;
import javafx.concurrent.Task;

/**
 * List puzzles task.
 */
final class ListPuzzlesTask extends Task<Void> {

    /** The puzzle service. */
    private final PuzzlePersistenceService puzzlePersistenceService;

    /**
     * Constructs an instance.
     *
     * @param puzzlePersistenceServiceArg the puzzle service
     */
    ListPuzzlesTask(final PuzzlePersistenceService puzzlePersistenceServiceArg) {
        puzzlePersistenceService = puzzlePersistenceServiceArg;
    }

    @Override
    protected Void call() {
        puzzlePersistenceService.list();
        return null;
    }
}