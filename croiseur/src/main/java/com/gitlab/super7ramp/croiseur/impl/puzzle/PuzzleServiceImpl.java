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
    public void load(final long puzzleId) {
        repository.query(puzzleId)
                  .ifPresentOrElse(presenter::presentLoadedPuzzle,
                                   () -> presenter.presentPuzzleRepositoryError(
                                           "Cannot load requested puzzle: No such puzzle exist"));
    }

    @Override
    public void save(final Puzzle puzzle) {
        repository.create(puzzle).ifPresent(presenter::presentSavedPuzzle);
        // Or else: Do nothing, error case is handle by SafePuzzleRepository
    }

    @Override
    public void save(final ChangedPuzzle puzzle) {
        repository.update(puzzle).ifPresent(presenter::presentSavedPuzzle);
        // Or else: Do nothing, error case is handle by SafePuzzleRepository
    }

    @Override
    public void patchAndSave(final PuzzlePatch patch) {
        final Optional<SavedPuzzle> optSavedPuzzle = repository.query(patch.id());
        if (optSavedPuzzle.isEmpty()) {
            presenter.presentPuzzleRepositoryError(
                    "Cannot modify puzzle with id #" + patch.id() + ": No such puzzle exists.");
            return;
        }
        final ChangedPuzzle changedPuzzle = patch(patch, optSavedPuzzle.get());
        repository.update(changedPuzzle).ifPresent(presenter::presentSavedPuzzle);
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
