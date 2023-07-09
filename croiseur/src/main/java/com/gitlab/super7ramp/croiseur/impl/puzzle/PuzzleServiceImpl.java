/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzlePatch;
import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.common.puzzle.ChangedPuzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.impl.puzzle.repository.SafePuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.PuzzleRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        final List<SavedPuzzle> puzzles = new ArrayList<>(repository.list());
        presenter.presentAvailablePuzzles(puzzles);
    }

    @Override
    public void delete(final long puzzleId) {
        repository.delete(puzzleId);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void load(final long puzzleId) {
        repository.query(puzzleId)
                  .ifPresentOrElse(presenter::presentLoadedPuzzle,
                                   () -> presenter.presentPuzzleRepositoryError(
                                           "Cannot load requested puzzle: Puzzle does not exist"));
    }

    @Override
    public void save(final Puzzle puzzle) {
        repository.create(puzzle);
        // SafePuzzleRepository.create() handles presentation for both success and error cases
    }

    @Override
    public void save(final ChangedPuzzle puzzle) {
        repository.update(puzzle);
        // SafePuzzleRepository.update() handles presentation for both success and error cases
    }

    @Override
    public void save(final long id, final PuzzlePatch patch) {
        final Optional<SavedPuzzle> optSavedPuzzle = repository.query(id);
        if (optSavedPuzzle.isEmpty()) {
            presenter.presentPuzzleRepositoryError(
                    "Failed to update puzzle: Cannot find saved puzzle with id " + id);
            return;
        }
        final ChangedPuzzle changedPuzzle = patch(patch, optSavedPuzzle.get());
        repository.update(changedPuzzle);
        // SafePuzzleRepository.update() handles presentation for both success and error cases
    }

    private static ChangedPuzzle patch(final PuzzlePatch modification,
                                       final SavedPuzzle savedPuzzle) {
        final PuzzleDetails details = patch(savedPuzzle.details(), modification);
        final PuzzleGrid grid = modification.modifiedGrid().orElseGet(savedPuzzle::grid);
        final Puzzle modifiedPuzzleData = new Puzzle(details, grid);
        return savedPuzzle.modifiedWith(modifiedPuzzleData);
    }

    private static PuzzleDetails patch(final PuzzleDetails original, final PuzzlePatch patch) {
        final String title = patch.modifiedTitle().orElseGet(original::title);
        final String author = patch.modifiedAuthor().orElseGet(original::author);
        final String editor = patch.modifiedEditor().orElseGet(original::editor);
        final String copyright = patch.modifiedCopyright().orElseGet(original::copyright);
        final Optional<LocalDate> date = patch.modifiedDate().or(original::date);
        return new PuzzleDetails(title, author, editor, copyright, date);
    }
}
