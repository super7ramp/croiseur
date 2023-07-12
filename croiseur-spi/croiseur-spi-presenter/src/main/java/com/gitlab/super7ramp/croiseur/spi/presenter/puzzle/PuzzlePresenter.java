/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.presenter.puzzle;

import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleCodecDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;

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
    void presentAvailablePuzzles(final List<SavedPuzzle> puzzles);

    /**
     * Present the puzzle loaded from repository.
     *
     * @param puzzle the loaded puzzle
     */
    void presentLoadedPuzzle(final SavedPuzzle puzzle);

    /**
     * Presents an error from the repository.
     *
     * @param error the error
     */
    void presentPuzzleRepositoryError(final String error);

    /**
     * Presents a puzzle that just has been saved.
     *
     * @param puzzle the saved puzzle
     */
    void presentSavedPuzzle(final SavedPuzzle puzzle);

    /**
     * Presents the confirmation that all puzzles have been deleted.
     */
    void presentDeletedAllPuzzles();

    /**
     * Presents the confirmation a puzzle which been deleted.
     *
     * @param id the id of the puzzle which has been deleted.
     */
    void presentDeletedPuzzle(final long id);

    // TODO move the following into a dedicated PuzzleImportPresenter?

    /**
     * Presents the available puzzle decoders.
     *
     * @param decoders the available puzzle coders
     */
    void presentPuzzleDecoders(final List<PuzzleCodecDetails> decoders);

    /**
     * Presents an error that occurred during puzzle import
     *
     * @param error the error
     */
    void presentPuzzleImportError(final String error);
}
