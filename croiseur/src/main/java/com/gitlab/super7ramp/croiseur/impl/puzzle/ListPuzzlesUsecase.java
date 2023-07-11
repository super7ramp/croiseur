/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.puzzle;

import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.impl.puzzle.repository.SafePuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * The 'list puzzles' usecase.
 */
final class ListPuzzlesUsecase {

    /** The puzzle repository. */
    private final SafePuzzleRepository repository;

    /** The puzzle presenter. */
    private final PuzzlePresenter presenter;

    /**
     * Constructs an instance.
     *
     * @param repositoryArg the puzzle repository
     * @param presenterArg  the puzzle presenter
     */
    ListPuzzlesUsecase(final SafePuzzleRepository repositoryArg,
                       final PuzzlePresenter presenterArg) {
        repository = repositoryArg;
        presenter = presenterArg;
    }

    /**
     * Processes the 'list puzzle' event.
     */
    void process() {
        final List<SavedPuzzle> puzzles = new ArrayList<>(repository.list());
        presenter.presentAvailablePuzzles(puzzles);
    }
}
