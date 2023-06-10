/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.puzzle.repository;

import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;

import java.util.Objects;

/**
 * Represents a changed puzzle, i.e. a puzzle that has been saved to repository then modified
 * without having been re-saved yet.
 *
 * @param id   the identifier of the data in the repository
 * @param data the modified puzzle data
 */
public record ChangedPuzzle(int id, Puzzle data) {

    /**
     * Validates fields.
     *
     * @param id   the identifier of the data in the repository
     * @param data the modified puzzle data
     */
    public ChangedPuzzle {
        Objects.requireNonNull(data);
    }
}
