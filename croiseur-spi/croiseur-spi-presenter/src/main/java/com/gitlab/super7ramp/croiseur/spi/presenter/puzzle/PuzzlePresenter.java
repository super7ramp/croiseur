/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.presenter.puzzle;

/**
 * Puzzle-related presentation services.
 */
public interface PuzzlePresenter {

    /**
     * Presents an error from the repository.
     *
     * @param error the error
     */
    void presentPuzzleRepositoryError(final String error);
}
