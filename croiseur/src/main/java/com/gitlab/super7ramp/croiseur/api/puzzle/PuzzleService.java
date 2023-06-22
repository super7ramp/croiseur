/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.api.puzzle;

import com.gitlab.super7ramp.croiseur.common.puzzle.ChangedPuzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;

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
    void delete(final long puzzleId);

    /**
     * Deletes all puzzles.
     */
    void deleteAll();

    /**
     * Loads the puzzle with given id from puzzle repository.
     *
     * @param puzzleId the puzzle id
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentLoadedPuzzle(com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle)
     * PuzzlePresenter#presentLoadedPuzzle(SavedPuzzle)
     */
    void load(final long puzzleId);

    /**
     * Saves the given puzzle to puzzle repository.
     * <p>
     * A new record will be added to the puzzle repository.
     *
     * @param puzzle the puzzle to save
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentSavedPuzzle(com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle)
     * presentSavedPuzzle(SavedPuzzle)
     */
    void save(final Puzzle puzzle);

    /**
     * Saves the given changed puzzle to puzzle repository.
     * <p>
     * No record will be added to the puzzle repository: The existing record will be updated.
     *
     * @param puzzle the puzzle to save
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentSavedPuzzle(com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle)
     * presentSavedPuzzle(SavedPuzzle)
     */
    void save(final ChangedPuzzle puzzle);

    /**
     * Same effect as {@link #save(ChangedPuzzle)}, but patch parameter contains only the
     * modifications since last save.
     * <p>
     * Method applies the given modifications then saves the result.
     *
     * @param puzzleId the identifier of the puzzle to patch and save
     * @param patch    the modifications to apply
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentSavedPuzzle(com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle)
     * presentSavedPuzzle(SavedPuzzle)
     */
    void save(final long puzzleId, final PuzzlePatch patch);
}
