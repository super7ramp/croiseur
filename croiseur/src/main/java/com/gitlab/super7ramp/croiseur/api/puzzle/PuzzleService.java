/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.api.puzzle;

import com.gitlab.super7ramp.croiseur.common.puzzle.ChangedPuzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Services pertaining to puzzle management.
 * <p>
 * Results of the requests are forwarded to the
 * {@link com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter PuzzlePresenter}.
 * <p>
 * All methods expect non-{@code null} parameters and raise a {@link NullPointerException} if given
 * a {@code null} parameter.
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
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentDeletedPuzzle(long)
     * PuzzlePresenter#presentDeletedPuzzle(long)
     */
    void delete(final long puzzleId);

    /**
     * Deletes all puzzles.
     *
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentDeletedAllPuzzles()
     * PuzzlePresenter#presentDeletedAllPuzzles
     */
    void deleteAll();

    /**
     * Loads the puzzle with given id from puzzle repository.
     * <p>
     * If no saved puzzle has the given id, an error will be presented.
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

    // TODO move the following to a dedicated (Puzzle)ImportService?

    /**
     * Lists the available puzzle decoders.
     *
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentPuzzleDecoders(List)
     * presentPuzzleDecoders(List)
     */
    void listDecoders();

    /**
     * Imports a puzzle from an input stream.
     * <p>
     * The given input stream will be read and decoded using available
     * {@link #listDecoders() decoders}, then saved to the puzzle repository.
     *
     * @param inputStream the input stream from which to decode the puzzle to import
     * @param format      the puzzle format - preferably the mimetype, if any, or the file
     *                    extension
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentSavedPuzzle(com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle)
     * presentSavedPuzzle(SavedPuzzle)
     */
    void importPuzzle(final String format, final InputStream inputStream);

    // TODO move the following to a dedicated (Puzzle)ExportService?

    /**
     * Lists the available puzzle encoders.
     *
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentPuzzleEncoders(List)
     * presentPuzzleEncoders(List)
     */
    void listEncoders();

    /**
     * Exports a puzzle from repository to given output stream.
     * <p>
     * The puzzle will be encoded using available {@link #listEncoders() encoders}, then saved to
     * the puzzle repository.
     * <p>
     * If no saved puzzle has the given id, an error will be presented.
     *
     * @param id           the id of the saved puzzle to export
     * @param format       the puzzle format - preferably the mimetype, if any, or the file
     *                     extension
     * @param outputStream the output stream where to encode the puzzle
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentSavedPuzzle(com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle)
     * presentSavedPuzzle(SavedPuzzle)
     */
    void exportPuzzle(final long id, final String format, final OutputStream outputStream);
}
