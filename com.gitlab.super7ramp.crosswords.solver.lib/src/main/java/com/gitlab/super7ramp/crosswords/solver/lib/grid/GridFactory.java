package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import com.gitlab.super7ramp.crosswords.solver.api.PuzzleDefinition;

/**
 * Factory for {@link Grid}.
 */
public final class GridFactory {

    /**
     * Private constructor, static factory methods only.
     */
    private GridFactory() {
        // Nothing to do.
    }

    /**
     * Create a puzzle from {@link PuzzleDefinition}
     *
     * @param puzzleDefinition a puzzle from {@link PuzzleDefinition}
     * @return the Puzzle
     */
    public static Grid createGrid(final PuzzleDefinition puzzleDefinition) {
        final GridData.GridDataBuilder gridBuilder = GridData.newBuilder();
        gridBuilder.from(puzzleDefinition);
        return new GridImpl(gridBuilder.build());
    }
}
