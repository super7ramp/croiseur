/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.grid;

import com.gitlab.super7ramp.croiseur.common.GridPosition;
import com.gitlab.super7ramp.croiseur.common.PuzzleDefinition;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;

import java.util.Map;
import java.util.Set;

/**
 * Access to the problem.
 */
public interface Grid {

    /**
     * Create a new grid from {@link PuzzleDefinition}.
     *
     * @param puzzleDefinition the puzzle definition
     * @return the new grid
     */
    static Grid create(final PuzzleDefinition puzzleDefinition) {
        final GridDataBuilder gridBuilder = new GridDataBuilder();
        gridBuilder.from(puzzleDefinition);
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
     * <p>
     * The returned map is immutable and only contains boxes with content (no {@code null} value).
     * Data can only be modified using {@link #puzzle()}.
     *
     * @return the grid
     */
    Map<GridPosition, Character> boxes();

    /**
     * Returns the positions owned by the given slot.
     * <p>
     * The returned set is not backed by the actual grid, hence adding or removing position to
     * the returned set has no effect. Data can only be modified using {@link #puzzle()}.
     *
     * @param slot the slot
     * @return the positions owned by the given slot
     */
    Set<GridPosition> slotPositions(final Slot slot);
}
