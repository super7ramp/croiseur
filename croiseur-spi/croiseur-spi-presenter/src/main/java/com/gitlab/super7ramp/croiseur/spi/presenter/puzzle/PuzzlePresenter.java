/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.presenter.puzzle;

import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;

import java.util.List;

/**
 * Puzzle-related presentation services.
 */
public interface PuzzlePresenter {

    /**
     * Present the available puzzles.
     *
     * @param puzzles the available puzzles
     */
    void presentAvailablePuzzles(final List<Puzzle> puzzles);

    /**
     * Present the puzzle loaded from repository.
     *
     * @param puzzle the loaded puzzle
     */
    void presentLoadedPuzzle(final Puzzle puzzle);

    /**
     * Presents an error from the repository.
     *
     * @param error the error
     */
    void presentPuzzleRepositoryError(final String error);
}
