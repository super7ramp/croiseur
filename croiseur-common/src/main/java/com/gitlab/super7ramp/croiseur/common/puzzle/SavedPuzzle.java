/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.common.puzzle;

import java.util.Objects;

/**
 * A puzzle which has been saved to repository.
 *
 * @param id       the unique identifier of the puzzle
 * @param data     the puzzle data
 * @param revision the puzzle revision (i.e. the number of repository updates for this identifier)
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
        return new ChangedPuzzle(id, newData);
    }

    /**
     * Returns the puzzle grid inside the {@link #data}.
     * <p>
     * A shortcut for {@code committedPuzzle.data().grid()}.
     *
     * @return puzzle grid inside the {@link #data}
     */
    public PuzzleGrid grid() {
        return data.grid();
    }

    /**
     * Returns the puzzle details inside the {@link #data}.
     * <p>
     * A shortcut for {@code committedPuzzle.data().details()}.
     *
     * @return puzzle details inside the {@link #data}
     */
    public PuzzleDetails details() {
        return data.details();
    }
}
