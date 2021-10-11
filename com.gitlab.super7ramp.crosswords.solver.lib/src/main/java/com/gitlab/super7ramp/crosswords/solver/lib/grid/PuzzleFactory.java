package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import com.gitlab.super7ramp.crosswords.solver.api.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.solver.lib.core.ProbablePuzzle;

public final class PuzzleFactory {

    /**
     * Private constructor, static factory methods only.
     */
    private PuzzleFactory() {
        // Nothing to do.
    }

    /**
     * Create a puzzle from {@link PuzzleDefinition}
     *
     * @param puzzleDefinition a puzzle from {@link PuzzleDefinition}
     * @return the Puzzle
     */
    public static ProbablePuzzle createPuzzle(final PuzzleDefinition puzzleDefinition) {
        final GridData.GridDataBuilder gridBuilder = GridData.newBuilder();
        gridBuilder.from(puzzleDefinition);
        return new PuzzleImpl(gridBuilder.build());
    }
}
