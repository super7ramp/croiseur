package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import com.gitlab.super7ramp.crosswords.solver.api.GridPosition;
import com.gitlab.super7ramp.crosswords.solver.api.PuzzleDefinition;

import java.util.Map;

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
     * The grid, i.e. the physical view of the problem.
     * <p>
     * The returned map is immutable. Data can only be modified using {@link #puzzle()}.
     *
     * @return the grid
     */
    Map<GridPosition, Character> boxes();
}
