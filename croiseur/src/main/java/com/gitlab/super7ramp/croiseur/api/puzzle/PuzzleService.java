/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.api.puzzle;

import java.util.List;

/**
 * Services pertaining to puzzle management.
 */
public interface PuzzleService {

    /**
     * Lists the available puzzles.
     *
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentAvailablePuzzles(List)
     * PuzzlePresenter#presentAvailablePuzzles
     */
    void list();

    /**
     * Deletes the puzzle with given puzzle id.
     *
     * @param puzzleId the puzzle id
     */
    void delete(final int puzzleId);

    /**
     * Displays the puzzle with given id.
     *
     * @param puzzleId the puzzle id
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentPuzzle }
     * PuzzlePresenter#presentPuzzle
     */
    void display(final int puzzleId);
}
