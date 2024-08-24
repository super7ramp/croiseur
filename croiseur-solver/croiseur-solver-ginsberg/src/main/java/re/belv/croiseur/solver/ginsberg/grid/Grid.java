/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.grid;

import java.util.Map;
import java.util.Set;
import re.belv.croiseur.common.puzzle.GridPosition;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.solver.ginsberg.core.Slot;

/** Access to the problem. */
public interface Grid {

    /**
     * Create a new grid from {@link PuzzleGrid}.
     *
     * @param puzzleGrid the puzzle definition
     * @return the new grid
     */
    static Grid create(final PuzzleGrid puzzleGrid) {
        final GridDataBuilder gridBuilder = GridDataBuilder.from(puzzleGrid);
        return new GridImpl(gridBuilder.build());
    }

    /**
     * The puzzle, i.e. the logical view of the problem.
     *
     * @return the puzzle
     */
    Puzzle puzzle();

    /**
     * The boxes, i.e. the main physical view of the problem.
     *
     * <p>The returned map is immutable and only contains boxes with content (no {@code null} value). Data can only be
     * modified using {@link #puzzle()}.
     *
     * @return the grid
     */
    Map<GridPosition, Character> boxes();

    /**
     * Returns the positions owned by the given slot.
     *
     * <p>The returned set is not backed by the actual grid, hence adding or removing position to the returned set has
     * no effect. Data can only be modified using {@link #puzzle()}.
     *
     * @param slot the slot
     * @return the positions owned by the given slot
     */
    Set<GridPosition> slotPositions(final Slot slot);
}
