/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.common.puzzle;

import java.util.Objects;

/**
 * A puzzle.
 *
 * @param details details about the puzzle
 * @param grid the grid definition
 * @param clues the clues
 */
public record Puzzle(PuzzleDetails details, PuzzleGrid grid, PuzzleClues clues) {

    /**
     * Validates fields.
     *
     * @param details details about the puzzle
     * @param grid the grid definition
     * @param clues the clues
     */
    public Puzzle {
        Objects.requireNonNull(details);
        Objects.requireNonNull(grid);
        Objects.requireNonNull(clues);
    }
}
