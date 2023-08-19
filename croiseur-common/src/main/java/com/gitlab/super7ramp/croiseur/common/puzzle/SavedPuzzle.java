/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.common.puzzle;

import java.util.Objects;

/**
 * A puzzle which has been saved to a repository.
 *
 * @param id       the unique identifier of the puzzle
 * @param data     the puzzle data
 * @param revision the puzzle revision (i.e. the number of updates for this identifier)
 */
public record SavedPuzzle(long id, Puzzle data, int revision) {

    /**
     * Validates fields
     *
     * @param id       the unique identifier of the puzzle
     * @param data     the puzzle data
     * @param revision the puzzle revision (i.e. the number of repository updates for this
     *                 identifier)
     */
    public SavedPuzzle {
        Objects.requireNonNull(data);
    }

    /**
     * Creates a new {@link ChangedPuzzle} from this saved version, using the given new puzzle
     * data.
     *
     * @param newData the new puzzle data
     * @return a new {@link ChangedPuzzle}
     */
    public ChangedPuzzle modifiedWith(final Puzzle newData) {
        Objects.requireNonNull(newData);
        return new ChangedPuzzle(id, newData);
    }

    /**
     * Creates a new {@link ChangedPuzzle} from this saved version, using the given new grid.
     * <p>
     * Same as {@link #modifiedWith(Puzzle)} but allows to only update the {@link #grid()}, without
     * touching the {@link #details()}.
     *
     * @param newGrid the new grid
     * @return a new {@link ChangedPuzzle}
     */
    public ChangedPuzzle modifiedWith(final PuzzleGrid newGrid) {
        final Puzzle newData = new Puzzle(details(), newGrid, clues());
        return modifiedWith(newData);
    }

    /**
     * Creates a new {@link ChangedPuzzle} from this saved version, using the given new details.
     * <p>
     * Same as {@link #modifiedWith(Puzzle)} but allows to only update the {@link #details()},
     * without touching the {@link #grid()}.
     *
     * @param newDetails the new details
     * @return a new {@link ChangedPuzzle}
     */
    public ChangedPuzzle modifiedWith(final PuzzleDetails newDetails) {
        final Puzzle newData = new Puzzle(newDetails, grid(), clues());
        return modifiedWith(newData);
    }

    /**
     * Returns a new {@link ChangedPuzzle} from this saved puzzle.
     *
     * @return a new {@link ChangedPuzzle} from this saved puzzle
     */
    public ChangedPuzzle asChangedPuzzle() {
        return new ChangedPuzzle(id, data);
    }

    /**
     * Returns the puzzle grid inside the {@link #data}.
     * <p>
     * A shortcut for {@code savedPuzzle.data().grid()}.
     *
     * @return puzzle grid inside the {@link #data}
     */
    public PuzzleGrid grid() {
        return data.grid();
    }

    /**
     * Returns the puzzle details inside the {@link #data}.
     * <p>
     * A shortcut for {@code savedPuzzle.data().details()}.
     *
     * @return puzzle details inside the {@link #data}
     */
    public PuzzleDetails details() {
        return data.details();
    }

    /**
     * Returns the puzzle clues inside the {@link #data}.
     * <p>
     * A shortcut for {@code savedPuzzle.data().clues()}.
     *
     * @return puzzle clues inside the {@link #data}
     */
    public PuzzleClues clues() {
        return data.clues();
    }
}
