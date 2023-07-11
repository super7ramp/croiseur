/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.puzzle;

import com.gitlab.super7ramp.croiseur.impl.puzzle.repository.SafePuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;

/**
 * The 'load puzzle' usecase.
 */
final class LoadPuzzleUsecase {

    /** The puzzle repository. */
    private final SafePuzzleRepository repository;

    /** The puzzle presenter. */
    private final PuzzlePresenter presenter;

    /**
     * Constructs an instance.
     *
     * @param repositoryArg the puzzle repository
     * @param presenterArg  the presenter
     */
    LoadPuzzleUsecase(final SafePuzzleRepository repositoryArg,
                      final PuzzlePresenter presenterArg) {
        repository = repositoryArg;
        presenter = presenterArg;
    }

    /**
     * Processes the 'load puzzle' request.
     *
     * @param puzzleId the id of the puzzle to load
     */
    void process(final long puzzleId) {
        repository.query(puzzleId)
                  .ifPresentOrElse(presenter::presentLoadedPuzzle,
                                   () -> presenter.presentPuzzleRepositoryError(
                                           "Cannot load requested puzzle: Puzzle does not exist"));
    }
}
