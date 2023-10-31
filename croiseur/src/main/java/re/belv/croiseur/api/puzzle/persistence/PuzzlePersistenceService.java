/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.api.puzzle.persistence;

import re.belv.croiseur.common.puzzle.ChangedPuzzle;
import re.belv.croiseur.common.puzzle.Puzzle;

import java.util.List;

/**
 * Services pertaining to puzzle persistence.
 * <p>
 * Results of the requests are forwarded to the
 * {@link re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter PuzzlePresenter}.
 * <p>
 * All methods expect non-{@code null} parameters and raise a {@link NullPointerException} if given
 * a {@code null} parameter.
 */
public interface PuzzlePersistenceService {

    /**
     * Lists the available puzzles.
     *
     * @see re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentAvailablePuzzles(List)
     * PuzzlePresenter#presentAvailablePuzzles
     */
    void list();

    /**
     * Deletes the puzzle with given puzzle id.
     *
     * @param puzzleId the puzzle id
     * @see re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentDeletedPuzzle(long)
     * PuzzlePresenter#presentDeletedPuzzle(long)
     */
    void delete(final long puzzleId);

    /**
     * Deletes all puzzles.
     *
     * @see re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentDeletedAllPuzzles()
     * PuzzlePresenter#presentDeletedAllPuzzles
     */
    void deleteAll();

    /**
     * Loads the puzzle with given id from puzzle repository.
     * <p>
     * If no saved puzzle has the given id, an error will be presented.
     *
     * @param puzzleId the puzzle id
     * @see re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentLoadedPuzzle(re.belv.croiseur.common.puzzle.SavedPuzzle)
     * PuzzlePresenter#presentLoadedPuzzle(SavedPuzzle)
     */
    void load(final long puzzleId);

    /**
     * Saves the given puzzle to puzzle repository.
     * <p>
     * A new record will be added to the puzzle repository.
     *
     * @param puzzle the puzzle to save
     * @see re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentSavedPuzzle(re.belv.croiseur.common.puzzle.SavedPuzzle)
     * PuzzlePresenter#presentSavedPuzzle(SavedPuzzle)
     */
    void save(final Puzzle puzzle);

    /**
     * Saves the given changed puzzle to puzzle repository.
     * <p>
     * No record will be added to the puzzle repository: The existing record will be updated.
     *
     * @param puzzle the puzzle to save
     * @see re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentSavedPuzzle(re.belv.croiseur.common.puzzle.SavedPuzzle)
     * PuzzlePresenter#presentSavedPuzzle(SavedPuzzle)
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
     * @see re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentSavedPuzzle(re.belv.croiseur.common.puzzle.SavedPuzzle)
     * PuzzlePresenter#presentSavedPuzzle(SavedPuzzle)
     */
    void save(final long puzzleId, final PuzzlePatch patch);

}
