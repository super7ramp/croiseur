package com.gitlab.super7ramp.crosswords.solver.paulgb.plugin;

import com.gitlab.super7ramp.crosswords.common.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.solver.paulgb.Grid;
import com.gitlab.super7ramp.crosswords.solver.paulgb.Solver;
import com.gitlab.super7ramp.crosswords.spi.solver.CrosswordSolver;
import com.gitlab.super7ramp.crosswords.spi.solver.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.solver.ProgressListener;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

import java.util.Optional;

/**
 * Implementation of {@link CrosswordSolver} adapting
 * {@link com.gitlab.super7ramp.crosswords.solver.paulgb.Solver paulgb's Crossword Composer}
 * solver}.
 */
public final class CrosswordComposerSolver implements CrosswordSolver {

    /** The adapted solver. */
    private final Solver solver;

    /**
     * Constructs an instance.
     */
    public CrosswordComposerSolver() {
        solver = new Solver();
    }

    /**
     * Creates a result for when the grid is impossible to solve.
     *
     * @param puzzle the input puzzle
     * @return a result for when the grid is impossible to solve
     */
    private static SolverResult impossible(final NumberedPuzzleDefinition puzzle) {
        return AdaptedSolverResult.impossible(puzzle.positionToId().keySet());
    }

    /**
     * Creates a result for when the grid has been successfully solved.
     *
     * @param puzzle the input puzzle
     * @param result the raw result
     * @return a result for when the grid is impossible to solve
     */
    private static SolverResult success(final NumberedPuzzleDefinition puzzle,
                                        final char[] result) {
        return AdaptedSolverResult.success(puzzle.idToPosition(), result);
    }

    // TODO try to make native code interruptible
    @Override
    public SolverResult solve(final PuzzleDefinition puzzle, final Dictionary dictionary,
                              final ProgressListener progressListener) {

        final NumberedPuzzleDefinition numberedPuzzle = new NumberedPuzzleDefinition(puzzle);
        if (!numberedPuzzle.filled().isEmpty()) {
            // Pre-filled grid is not supported by Crossword Composer
            return impossible(numberedPuzzle);
        }
        final com.gitlab.super7ramp.crosswords.solver.paulgb.Dictionary adaptedDictionary =
                DictionaryAdapter.adapt(dictionary);
        final Grid adaptedPuzzle = GridAdapter.adapt(numberedPuzzle);

        final Optional<char[]> optResult = solver.solve(adaptedPuzzle, adaptedDictionary);

        return optResult.map(result -> success(numberedPuzzle, result))
                        .orElseGet(() -> impossible(numberedPuzzle));
    }


}
