/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.common.puzzle;

import java.util.Objects;

/**
 * Represents a changed puzzle, i.e. a puzzle that has been saved to a repository then modified
 * without having been re-saved yet.
 */
public final class ChangedPuzzle {

    /** The unique identifier of the puzzle. */
    private final long id;

    /** The modified puzzle data. */
    private final Puzzle data;

    /**
     * Validates fields.
     *
     * @param idArg   the unique identifier of the puzzle
     * @param dataArg the modified puzzle data
     */
    ChangedPuzzle(final long idArg, final Puzzle dataArg) {
        id = idArg;
        data = Objects.requireNonNull(dataArg);
    }

    /**
     * Returns the modified puzzle data.
     *
     * @return the modified puzzle data
     */
    public Puzzle data() {
        return data;
    }

    /**
     * Returns the unique identifier of the puzzle.
     *
     * @return the unique identifier of the puzzle
     */
    public long id() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ChangedPuzzle that)) return false;
        return id == that.id && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, data);
    }

    @Override
    public String toString() {
        return "ChangedPuzzle{" +
               "id=" + id +
               ", data=" + data +
               '}';
    }
}
