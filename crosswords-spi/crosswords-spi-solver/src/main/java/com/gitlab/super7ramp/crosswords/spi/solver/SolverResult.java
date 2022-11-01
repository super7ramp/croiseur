package com.gitlab.super7ramp.crosswords.spi.solver;

import com.gitlab.super7ramp.crosswords.common.GridPosition;

import java.util.Map;

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
     * @return the result kind
     */
    Kind kind();

    /**
     * The boxes of the puzzle filled if {@link #kind()} is {@link Kind#SUCCESS}; the last known
     * state otherwise.
     *
     * @return The boxes of the puzzle filled if {@link #kind()} is {@link Kind#SUCCESS}; the
     * last known state otherwise
     */
    Map<GridPosition, Character> boxes();

    /**
     * Statistics about the resolution.
     *
     * @return statistics about the resolution
     */
    Statistics statistics();
}
