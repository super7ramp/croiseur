/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.puzzle.repository;

import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;

import java.util.Collection;
import java.util.Optional;

/**
 * A puzzle repository.
 * <p>
 * It provides basic CRUD operations. It uses 3 types:
 * <ul>
 *     <li>{@link Puzzle}: The puzzle data.</li>
 *     <li>{@link SavedPuzzle}: A {@link Puzzle} that has been successfully saved to repository.
 *     It augments {@link Puzzle} with a record identifier and a revision number both determined by
 *     the repository.</li>
 *     <li>{@link ChangedPuzzle}: A {@link SavedPuzzle} that has been modified since its last save
 *     to repository.</li>
 * </ul>
 * <p>
 * Example of usage:
 * <pre>{@code
 * // Creation part
 * final Puzzle puzzleData = puzzleData();
 * SavedPuzzle savedPuzzle = repository.create(puzzleData);
 *
 * // Update part
 * final Puzzle modifiedPuzzleData = modifiedPuzzle();
 * final ChangedPuzzle changedPuzzle = savedPuzzle.modifiedWith(modifiedPuzzleData);
 * savedPuzzle = repository.update(changedPuzzle);
 * }</pre>
 */
public interface PuzzleRepository {

    /**
     * Creates a new data.
     *
     * @param puzzle the new data
     * @return the {@link SavedPuzzle} representing the data committed to the repository (just the
     * given data with an identifier determined by the repository implementation)
     * @throws NullPointerException if given data is {@code null}
     * @throws WriteException       if given puzzle cannot be written to repository for another
     *                              reason
     * @implSpec The returned {@link SavedPuzzle} shall contain a new unique identifier of the
     * puzzle. The revision number shall be 1.
     */
    SavedPuzzle create(final Puzzle puzzle) throws WriteException;

    /**
     * Updates an existing data.
     *
     * @param changedPuzzle the modified data
     * @return the saved puzzle containing the updates from given changed puzzle
     * @throws NullPointerException if given data is {@code null}
     * @throws WriteException       if given puzzle cannot be written to repository for another
     *                              reason
     * @implSpec The returned {@link SavedPuzzle} shall contain an incremented revision number if
     * data has actually changed, i.e. if data inside {@code changedPuzzle} differs from
     * corresponding data in repository before the call to this method. The revision number should
     * not be incremented when data has not actually changed.
     */
    SavedPuzzle update(final ChangedPuzzle changedPuzzle) throws WriteException;

    /**
     * Deletes the given saved puzzle.
     *
     * @param puzzle the saved puzzle to delete
     * @throws NullPointerException if given puzzle is {@code null}
     * @throws WriteException       if given puzzle cannot be deleted from repository
     */
    default void delete(final SavedPuzzle puzzle) throws WriteException {
        delete(puzzle.id());
    }

    /**
     * Deletes the saved puzzle identified by given id.
     *
     * @param id the identifier of the saved puzzle to delete
     * @throws WriteException       if given puzzle cannot be deleted from repository
     */
    void delete(final int id) throws WriteException;

    /**
     * Retrieves the saved puzzle with given ID.
     *
     * @param id the id of the saved puzzle to retrieve
     * @return the saved puzzle with given ID, if any, or {@link Optional#empty()}
     */
    Optional<SavedPuzzle> query(final int id);

    /**
     * Lists all the data present in this repository.
     *
     * @return the puzzles present in this repository
     */
    Collection<SavedPuzzle> list();
}
