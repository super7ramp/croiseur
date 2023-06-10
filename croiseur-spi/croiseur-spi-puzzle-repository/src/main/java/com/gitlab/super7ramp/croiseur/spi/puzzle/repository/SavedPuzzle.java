/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.puzzle.repository;

import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;

/**
 * A puzzle which has been saved to repository.
 *
 * @param id       the identifier of the puzzle in the repository
 * @param data     the puzzle data
 * @param revision the puzzle revision (i.e. the number of repository updates for this identifier)
 */
public record SavedPuzzle(int id, Puzzle data, int revision) {

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
