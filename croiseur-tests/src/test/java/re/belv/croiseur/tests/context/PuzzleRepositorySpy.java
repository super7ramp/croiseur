/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.tests.context;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import re.belv.croiseur.common.puzzle.ChangedPuzzle;
import re.belv.croiseur.common.puzzle.Puzzle;
import re.belv.croiseur.common.puzzle.SavedPuzzle;
import re.belv.croiseur.spi.puzzle.repository.PuzzleRepository;
import re.belv.croiseur.spi.puzzle.repository.WriteException;

/** James Bond's retirement. */
public final class PuzzleRepositorySpy implements PuzzleRepository {

    /** The spied repository. */
    private final PuzzleRepository spied;

    /** The list of unverified creations. */
    private final List<SavedPuzzle> creations;

    /** The list of unverified updates. */
    private final List<SavedPuzzle> updates;

    /** The list of unverified deletions. */
    private final List<Long> deletions;

    /** The list of created puzzle ids. */
    private final List<Long> createdIds;

    /**
     * Constructs an instance.
     *
     * @param puzzleRepository the spied repository
     */
    PuzzleRepositorySpy(final PuzzleRepository puzzleRepository) {
        spied = puzzleRepository;
        creations = new LinkedList<>();
        updates = new LinkedList<>();
        deletions = new LinkedList<>();
        createdIds = new ArrayList<>();
    }

    @Override
    public SavedPuzzle create(final Puzzle puzzle) throws WriteException {
        final SavedPuzzle created = spied.create(puzzle);
        creations.add(created);
        createdIds.add(created.id());
        return created;
    }

    @Override
    public SavedPuzzle update(final ChangedPuzzle changedPuzzle) throws WriteException {
        final SavedPuzzle updated = spied.update(changedPuzzle);
        updates.add(updated);
        return updated;
    }

    @Override
    public void delete(final long puzzleId) throws WriteException {
        spied.delete(puzzleId);
        deletions.add(puzzleId);
    }

    @Override
    public Optional<SavedPuzzle> query(final long id) {
        return spied.query(id);
    }

    @Override
    public Collection<SavedPuzzle> list() {
        return spied.list();
    }

    /**
     * Verifies that the given puzzle has been created.
     *
     * @param expected the expected created puzzle
     * @throws AssertionError if verification fails
     */
    public void verifyCreation(final SavedPuzzle expected) {
        final boolean removed = creations.remove(expected);
        assertTrue(removed, () -> "No creation of " + expected + " recorded. Recorded creations are: " + creations);
    }

    /**
     * Verifies that the given puzzle has been updated.
     *
     * @param expected the expected updated puzzle
     * @throws AssertionError if verification fails
     */
    public void verifyUpdate(final SavedPuzzle expected) {
        final boolean removed = updates.remove(expected);
        assertTrue(removed, () -> "No update of " + expected + " recorded. Recorded updates are: " + updates);
    }

    /**
     * Verifies that the puzzle with given id has been deleted.
     *
     * @param expected the expected updated puzzle
     * @throws AssertionError if verification fails
     */
    public void verifyDeletion(final long expected) {
        final boolean removed = deletions.remove(expected);
        assertTrue(removed, () -> "No update of " + expected + " recorded. Recorded deletions are: " + deletions);
    }

    /**
     * Verifies that no more interactions have occurred on the repository.
     *
     * @throws AssertionError if verification fails
     */
    public void verifyNoMoreInteractions() {
        assertAll(
                () -> assertTrue(creations.isEmpty(), () -> "Unverified puzzle repository creation(s): " + creations),
                () -> assertTrue(updates.isEmpty(), () -> "Unverified puzzle repository update(s): " + updates),
                () -> assertTrue(deletions.isEmpty(), () -> "Unverified puzzle repository deletion(s): " + deletions));
    }

    /**
     * Retrieves the captured value of the id variable referenced by its given key.
     *
     * @param idVariableKey the id variable key (0 corresponds to the first record created, 1 to the second, and so on)
     * @return the captured id variable value, if any
     */
    public Optional<Long> idVariableValue(final int idVariableKey) {
        final Optional<Long> value;
        if (idVariableKey >= 0 && idVariableKey < createdIds.size()) {
            value = Optional.of(createdIds.get(idVariableKey));
        } else {
            value = Optional.empty();
        }
        return value;
    }
}
