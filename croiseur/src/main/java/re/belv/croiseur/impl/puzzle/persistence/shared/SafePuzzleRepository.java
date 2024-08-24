/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.puzzle.persistence.shared;

import java.util.Collection;
import java.util.Optional;
import re.belv.croiseur.common.puzzle.ChangedPuzzle;
import re.belv.croiseur.common.puzzle.Puzzle;
import re.belv.croiseur.common.puzzle.SavedPuzzle;
import re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import re.belv.croiseur.spi.puzzle.repository.PuzzleRepository;
import re.belv.croiseur.spi.puzzle.repository.WriteException;

/**
 * A {@link PuzzleRepository} wrapper that catches {@link WriteException} - turning the results into
 * {@link Optional}s - and calls the presentation service to present the errors as well as success
 * notifications.
 * <p>
 * It basically allows to share repository-related behaviours across services.
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
    public SafePuzzleRepository(final PuzzleRepository repositoryArg, final PuzzlePresenter presenterArg) {
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
            presenter.presentSavedPuzzle(savedPuzzle);
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
     */
    public Optional<SavedPuzzle> update(final ChangedPuzzle changedPuzzle) {
        try {
            final SavedPuzzle savedPuzzle = repository.update(changedPuzzle);
            presenter.presentSavedPuzzle(savedPuzzle);
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
            presenter.presentDeletedPuzzle(id);
        } catch (final WriteException e) {
            presenter.presentPuzzleRepositoryError("Failed to delete puzzle: " + e.getMessage());
        }
    }

    /**
     * Deletes all the puzzles.
     */
    public void deleteAll() {
        try {
            repository.deleteAll();
            presenter.presentDeletedAllPuzzles();
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
