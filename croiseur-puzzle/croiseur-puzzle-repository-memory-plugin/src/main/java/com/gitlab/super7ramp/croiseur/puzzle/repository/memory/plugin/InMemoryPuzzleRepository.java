/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.repository.memory.plugin;

import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.ChangedPuzzle;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.PuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.WriteException;

import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * An implementation of {@link PuzzleRepository} whose storage is purely in memory.
 */
public final class InMemoryPuzzleRepository implements PuzzleRepository {

    /** The free and used ids. */
    private final BitSet availableIds;

    /** The stored puzzles. */
    private final Map<Integer, SavedPuzzle> puzzles;

    /**
     * Constructs an instance.
     */
    public InMemoryPuzzleRepository() {
        puzzles = new HashMap<>();
        availableIds = new BitSet();
    }

    @Override
    public SavedPuzzle create(final Puzzle puzzle) {
        final SavedPuzzle savedPuzzle = new SavedPuzzle(nextId(), puzzle, 1);
        puzzles.put(savedPuzzle.id(), savedPuzzle);
        return savedPuzzle;
    }

    @Override
    public SavedPuzzle update(final ChangedPuzzle changedPuzzle) {
        final int id = changedPuzzle.id();
        final SavedPuzzle oldSavedPuzzle = savedPuzzleOrThrow(id);
        if (oldSavedPuzzle.data().equals(changedPuzzle.data())) {
            // No need to update
            return oldSavedPuzzle;
        }
        final SavedPuzzle newSavedPuzzle =
                new SavedPuzzle(id, changedPuzzle.data(), oldSavedPuzzle.revision() + 1);
        puzzles.put(id, newSavedPuzzle);
        return newSavedPuzzle;
    }

    @Override
    public void delete(final int puzzleId) throws WriteException {
        final SavedPuzzle deletedPuzzle = puzzles.remove(puzzleId);
        if (deletedPuzzle == null) {
            throw new WriteException("Cannot delete a non-existing puzzle");
        }
        availableIds.flip(puzzleId);
    }

    @Override
    public Optional<SavedPuzzle> query(final int id) {
        return Optional.ofNullable(puzzles.get(id));
    }

    @Override
    public Collection<SavedPuzzle> list() {
        return Collections.unmodifiableCollection(puzzles.values());
    }

    /**
     * Retrieves the currently saved puzzle with given id.
     *
     * @param id the id of the saved puzzle to retrieve
     * @return the saved puzzle
     * @throws IllegalArgumentException if no saved puzzle with given id exists
     */
    private SavedPuzzle savedPuzzleOrThrow(final int id) {
        final SavedPuzzle oldSavedPuzzle = puzzles.get(id);
        if (oldSavedPuzzle == null) {
            throw new IllegalArgumentException("Illegal attempt to get a non-existing puzzle");
        }
        return oldSavedPuzzle;
    }

    /**
     * Returns the next free id, starting at 1.
     *
     * @return the next free id.
     */
    private int nextId() {
        final int nextId = availableIds.nextClearBit(1 /* 0 is unused */);
        availableIds.flip(nextId);
        return nextId;
    }
}
