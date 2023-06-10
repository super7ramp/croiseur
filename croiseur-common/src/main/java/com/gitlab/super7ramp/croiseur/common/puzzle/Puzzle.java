/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.common.puzzle;

import java.util.Objects;

/**
 * A puzzle.
 *
 * @param details details about the puzzle
 * @param grid the grid definition
 */
public record Puzzle(PuzzleDetails details, PuzzleGrid grid) {

    /**
     * Validates fields.
     *
     * @param details details about the puzzle
     * @param grid the grid definition
     */
    public Puzzle {
        Objects.requireNonNull(details);
        Objects.requireNonNull(grid);
    }
    // Will include clues, when support for clues is added
}
