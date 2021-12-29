package com.gitlab.super7ramp.crosswords.solver.api;

import java.util.Map;

/**
 * Puzzle solving result.
 */
public interface SolverResult {

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
}
