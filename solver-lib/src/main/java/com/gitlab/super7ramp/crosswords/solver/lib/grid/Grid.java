package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import com.gitlab.super7ramp.crosswords.solver.api.Coordinate;

import java.util.Map;

/**
 * Access to the problem.
 */
public interface Grid {

    /**
     * The puzzle, i.e. the logical view of the problem.
     *
     * @return the puzzle
     */
    Puzzle puzzle();

    /**
     * The grid, i.e. the physical view of the problem.
     * <p>
     * The returned map is immutable. Data can only be modified using {@link #puzzle()}.
     *
     * @return the grid
     */
    Map<Coordinate, Character> boxes();
}
