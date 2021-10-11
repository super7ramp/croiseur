package com.gitlab.super7ramp.crosswords.solver.api;

import java.util.Map;

/**
 * Puzzle solving result.
 */
public interface SolverResult {

    /**
     * The boxes of the puzzle filled.
     *
     * @return the boxes of the puzzle filled
     */
    Map<Coordinate, Character> boxes();
}
