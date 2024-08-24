/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.repository.memory.plugin;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import re.belv.croiseur.common.puzzle.ChangedPuzzle;
import re.belv.croiseur.common.puzzle.Puzzle;
import re.belv.croiseur.common.puzzle.SavedPuzzle;
import re.belv.croiseur.spi.puzzle.repository.PuzzleRepository;
import re.belv.croiseur.spi.puzzle.repository.WriteException;

/**
 * An implementation of {@link PuzzleRepository} whose storage is purely in memory.
 */
public final class InMemoryPuzzleRepository implements PuzzleRepository {

    /** The free and used ids. */
    private final BitSet availableIds;

    /** The stored puzzles. */
    private final Map<Long, SavedPuzzle> puzzles;

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
    public SavedPuzzle update(final ChangedPuzzle changedPuzzle) throws WriteException {
        final long id = changedPuzzle.id();
        final SavedPuzzle oldSavedPuzzle =
                query(id).orElseThrow(() -> new WriteException("Cannot find saved puzzle with id " + id));
        if (oldSavedPuzzle.data().equals(changedPuzzle.data())) {
            // No need to update
            return oldSavedPuzzle;
        }
        final SavedPuzzle newSavedPuzzle = new SavedPuzzle(id, changedPuzzle.data(), oldSavedPuzzle.revision() + 1);
        puzzles.put(id, newSavedPuzzle);
        return newSavedPuzzle;
    }

    @Override
    public void delete(final long puzzleId) throws WriteException {
        final SavedPuzzle deletedPuzzle = puzzles.remove(puzzleId);
        if (deletedPuzzle == null) {
            throw new WriteException("Cannot delete a non-existing puzzle");
        }
        availableIds.flip((int) puzzleId);
    }

    @Override
    public void deleteAll() {
        puzzles.clear();
        availableIds.clear();
    }

    @Override
    public Optional<SavedPuzzle> query(final long id) {
        return Optional.ofNullable(puzzles.get(id));
    }

    @Override
    public Collection<SavedPuzzle> list() {
        return new ArrayList<>(puzzles.values());
    }

    /**
     * Returns the next free id, starting at 1.
     *
     * @return the next free id.
     */
    private long nextId() {
        final int nextId = availableIds.nextClearBit(1 /* 0 is unused */);
        availableIds.flip(nextId);
        return nextId;
    }
}
