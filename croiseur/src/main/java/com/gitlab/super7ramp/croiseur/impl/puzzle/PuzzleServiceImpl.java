/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.impl.puzzle.repository.SafePuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.PuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.SavedPuzzle;

import java.util.List;

/**
 * Implementation of {@link PuzzleService}.
 * <p>
 * Mostly boilerplate between {@link PuzzleRepository} and {@link PuzzlePresenter}.
 */
public final class PuzzleServiceImpl implements PuzzleService {

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
    public PuzzleServiceImpl(final PuzzleRepository repositoryArg,
                             final PuzzlePresenter presenterArg) {
        repository = new SafePuzzleRepository(repositoryArg, presenterArg);
        presenter = presenterArg;
    }

    @Override
    public void list() {
        final List<Puzzle> puzzles = repository.list().stream().map(SavedPuzzle::data).toList();
        presenter.presentAvailablePuzzles(puzzles);
    }

    @Override
    public void delete(final int puzzleId) {
        repository.delete(puzzleId);
    }

    @Override
    public void display(final int puzzleId) {
        repository.query(puzzleId)
                  .map(SavedPuzzle::data)
                  .ifPresentOrElse(presenter::presentPuzzle,
                                   () -> presenter.presentPuzzleRepositoryError(
                                           "Cannot display requested puzzle: No such puzzle exist"));
    }
}
