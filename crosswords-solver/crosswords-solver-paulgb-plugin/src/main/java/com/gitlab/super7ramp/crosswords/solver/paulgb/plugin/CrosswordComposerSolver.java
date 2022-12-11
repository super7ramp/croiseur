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
     * {@inheritDoc}
     *
     * @throws UnsupportedOperationException if the grid contains pre-filled cells: It is not
     *                                       supported by Crossword Composer
     */
    // TODO try to make native code interruptible
    @Override
    public SolverResult solve(final PuzzleDefinition puzzle, final Dictionary dictionary,
                              final ProgressListener progressListener) {

        final com.gitlab.super7ramp.crosswords.solver.paulgb.Dictionary adaptedDictionary =
                DictionaryAdapter.adapt(dictionary);
        final NumberedPuzzleDefinition numberedPuzzle = new NumberedPuzzleDefinition(puzzle);
        final Grid adaptedPuzzle = GridAdapter.adapt(numberedPuzzle);

        final Optional<char[]> optResult = solver.solve(adaptedPuzzle, adaptedDictionary);

        return optResult.map(result -> AdaptedSolverResult.success(numberedPuzzle.idToPosition(), result))
                        .orElseGet(() -> AdaptedSolverResult.impossible(numberedPuzzle.idToPosition()));
    }
}
