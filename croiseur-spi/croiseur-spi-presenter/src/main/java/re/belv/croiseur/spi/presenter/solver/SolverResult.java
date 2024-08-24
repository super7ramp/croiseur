/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.spi.presenter.solver;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import re.belv.croiseur.common.puzzle.GridPosition;
import re.belv.croiseur.common.puzzle.PuzzleGrid;

/**
 * A presentable solver result.
 *
 * @param isSuccess       whether solver found a solution
 * @param grid            the filled (or partially filled) grid
 * @param unsolvableBoxes the unsolvable boxes, if any
 */
public record SolverResult(boolean isSuccess, PuzzleGrid grid, Set<GridPosition> unsolvableBoxes) {

    /**
     * Validates fields.
     *
     * @param isSuccess       whether solver found a solution
     * @param grid            the filled (or partially filled) grid
     * @param unsolvableBoxes the unsolvable boxes, if any
     * @throws NullPointerException if any of the field is {@code null}
     * @throws IllegalArgumentException if result is a success and unsolvableBoxes is not empty
     */
    public SolverResult {
        Objects.requireNonNull(grid);
        Objects.requireNonNull(unsolvableBoxes);
        if (isSuccess && !unsolvableBoxes.isEmpty()) {
            throw new IllegalArgumentException(
                    "A solver result cannot be a success if unsolvable boxes have been found.");
        }
    }

    /**
     * Returns the puzzle grid filled boxes.
     * <p>
     * A shortcut for {@code solverResult.grid().filled()}.
     *
     * @return the puzzle grid filled boxes
     */
    public Map<GridPosition, Character> filledBoxes() {
        return grid.filled();
    }
}
