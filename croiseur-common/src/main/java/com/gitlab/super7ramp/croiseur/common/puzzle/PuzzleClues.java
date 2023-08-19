/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.common.puzzle;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Crossword puzzle clues.
 *
 * @param across the clues for the across (= horizontal) slots
 * @param down   the clues for the down (= vertical) slots
 */
public record PuzzleClues(List<String> across, List<String> down) {

    /** Empty clues unique instance. */
    private static final PuzzleClues EMPTY =
            new PuzzleClues(Collections.emptyList(), Collections.emptyList());

    /**
     * Validates fields.
     *
     * @param across the clues for the across (= horizontal) slots
     * @param down   the clues for the down (= vertical) slots
     */
    public PuzzleClues {
        Objects.requireNonNull(across);
        Objects.requireNonNull(down);
    }

    /**
     * Returns empty puzzle clues.
     *
     * @return empty puzzle clues
     */
    public static PuzzleClues empty() {
        return EMPTY;
    }
}
