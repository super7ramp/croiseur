/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzlePatch;
import com.gitlab.super7ramp.croiseur.common.puzzle.ChangedPuzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.impl.puzzle.repository.SafePuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;

import java.time.LocalDate;
import java.util.Optional;

/**
 * The 'patch and save' puzzle usecase.
 */
final class PatchAndSavePuzzleUsecase {

    /** The puzzle repository. */
    private final SafePuzzleRepository repository;

    /** The presenter. */
    private final PuzzlePresenter presenter;

    /**
     * Constructs an instance.
     *
     * @param repositoryArg the puzzle repository
     * @param presenterArg  the presenter
     */
    PatchAndSavePuzzleUsecase(final SafePuzzleRepository repositoryArg,
                              final PuzzlePresenter presenterArg) {
        presenter = presenterArg;
        repository = repositoryArg;
    }

    /**
     * Processes the given puzzle patch.
     *
     * @param id    the puzzle id
     * @param patch the puzzle patch
     */
    void process(final long id, final PuzzlePatch patch) {
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
