/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.puzzle.repository;

import com.gitlab.super7ramp.croiseur.common.puzzle.ChangedPuzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.PuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.WriteException;

import java.util.Collection;
import java.util.Optional;

/**
 * A {@link PuzzleRepository} wrapper that catches {@link WriteException} - turning the results into
 * {@link Optional}s - and calls the presentation service to present the errors.
 */
public final class SafePuzzleRepository {

    /** The actual repository. */
    private final PuzzleRepository repository;

    /** The presentation services. */
    private final PuzzlePresenter presenter;

    /**
     * Constructs an instance.
     *
     * @param repositoryArg the actual repository
     * @param presenterArg  the presenter
     */
    public SafePuzzleRepository(final PuzzleRepository repositoryArg,
                                final PuzzlePresenter presenterArg) {
        repository = repositoryArg;
        presenter = presenterArg;
    }

    /**
     * Creates a new data.
     *
     * @param puzzle the new data
     * @return the {@link SavedPuzzle} representing the data committed to the repository (just the
     * given data with an additional identifier), or {@link Optional#empty()} if write failed
     * @throws NullPointerException if given data is {@code null}
     */
    public Optional<SavedPuzzle> create(final Puzzle puzzle) {
        try {
            final SavedPuzzle savedPuzzle = repository.create(puzzle);
            return Optional.of(savedPuzzle);
        } catch (final WriteException e) {
            presenter.presentPuzzleRepositoryError("Failed to create puzzle: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Updates an existing data.
     *
     * @param changedPuzzle the modified data
     * @return the updated committed puzzle, or {@link Optional#empty()} if write failed
     * @throws NullPointerException if given data is {@code null}
     * @implSpec Repository shall increment the {@link SavedPuzzle#revision() revision number} when
     * this method is called if data has actually changed. Repository should not increment it when
     * data has not actually changed.
     */
    public Optional<SavedPuzzle> update(final ChangedPuzzle changedPuzzle) {
        try {
            final SavedPuzzle savedPuzzle = repository.update(changedPuzzle);
            return Optional.of(savedPuzzle);
        } catch (final WriteException e) {
            presenter.presentPuzzleRepositoryError("Failed to update puzzle: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Retrieves the puzzle saved with given ID.
     *
     * @param id the id of the saved puzzle to retrieve
     * @return the saved puzzle with given ID, if any, or {@link Optional#empty()}
     */
    public Optional<SavedPuzzle> query(final long id) {
        return repository.query(id);
    }

    /**
     * Deletes the saved puzzle identified with given id.
     *
     * @param id the id of the saved puzzle to delete
     */
    public void delete(final long id) {
        try {
            repository.delete(id);
        } catch (final WriteException e) {
            presenter.presentPuzzleRepositoryError("Failed to delete puzzle: " + e.getMessage());
        }
    }

    /**
     * Deletes all the puzzles.
     */
    public void deletetAll() {
        try {
            repository.deleteAll();
        } catch (final WriteException e) {
            presenter.presentPuzzleRepositoryError("Failed to delete puzzle(s): " + e.getMessage());
        }
    }

    /**
     * Lists all the data present in this repository.
     *
     * @return the puzzles present in this repository
     */
    public Collection<SavedPuzzle> list() {
        return repository.list();
    }

}
