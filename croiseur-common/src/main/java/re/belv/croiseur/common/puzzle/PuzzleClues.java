/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.common.puzzle;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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
     * Creates a new {@link PuzzleClues} from the given puzzle grid and given clues.
     *
     * @param clues the clues
     * @param grid  the grid
     * @return a new {@link PuzzleClues}
     */
    public static PuzzleClues from(final Map<String, String> clues, final PuzzleGrid grid) {
        final List<String> across =
                grid.acrossSlotContents().stream().map(word -> clues.getOrDefault(word, ""))
                    .toList();
        final List<String> down =
                grid.downSlotContents().stream().map(word -> clues.getOrDefault(word, "")).toList();
        return new PuzzleClues(across, down);
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
