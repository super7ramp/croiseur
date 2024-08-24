/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg;

import java.util.Map;
import java.util.Set;
import re.belv.croiseur.common.puzzle.GridPosition;

/**
 * Puzzle solving result.
 */
public interface SolverResult {

    /**
     * Solver statistics.
     */
    interface Statistics {

        /**
         * The total number of assignments (= variable instantiations).
         *
         * @return the total number of assignments.
         */
        long numberOfAssignments();

        /**
         * The total number of unassignments.
         *
         * @return the total number of unassignments
         */
        long numberOfUnassignments();

        /**
         * The size of the elimination set (~ blacklist) at the end of the computation.
         *
         * @return the size of the elimination set at the end of the computation
         */
        long eliminationSetSize();
    }

    /**
     * Kind of result.
     */
    enum Kind {
        /** The grid has been successfully solved. */
        SUCCESS,
        /** No solution exists. */
        IMPOSSIBLE
    }

    /**
     * Returns the result kind.
     *
     * @return the result kind
     */
    Kind kind();

    /**
     * The filled boxes.
     * <p>
     * Contains the entire grid filled if {@link #kind()} is
     * {@link Kind#SUCCESS}.
     * <p>
     * When {@link #kind()} is {@link Kind#IMPOSSIBLE}, the returned map contains only the boxes
     * that have been successfully filled, either by the solver or pre-filled. A special situation
     * is when a pre-filled box is not in the dictionary: In this case, the box will be here as
     * well as in {@link #unsolvableBoxes()}.
     *
     * @return the filled boxes
     */
    Map<GridPosition, Character> filledBoxes();

    /**
     * Returns the boxes for which no solution could be found or an empty set if {@link #kind()} is
     * {@link Kind#SUCCESS}
     *
     * @return the boxes for which no solution could be found
     */
    Set<GridPosition> unsolvableBoxes();

    /**
     * Statistics about the resolution.
     *
     * @return statistics about the resolution
     */
    Statistics statistics();
}
