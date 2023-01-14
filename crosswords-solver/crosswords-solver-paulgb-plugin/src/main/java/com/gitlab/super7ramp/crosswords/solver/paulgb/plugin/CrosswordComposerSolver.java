/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.solver.paulgb.plugin;

import com.gitlab.super7ramp.crosswords.common.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.solver.paulgb.Puzzle;
import com.gitlab.super7ramp.crosswords.solver.paulgb.Solution;
import com.gitlab.super7ramp.crosswords.solver.paulgb.Solver;
import com.gitlab.super7ramp.crosswords.solver.paulgb.SolverErrorException;
import com.gitlab.super7ramp.crosswords.spi.solver.CrosswordSolver;
import com.gitlab.super7ramp.crosswords.spi.solver.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.solver.ProgressListener;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of {@link CrosswordSolver} adapting
 * {@link com.gitlab.super7ramp.crosswords.solver.paulgb.Solver paulgb's Crossword Composer}
 * solver}.
 */
public final class CrosswordComposerSolver implements CrosswordSolver {

    /** The logger. */
    private static final Logger LOGGER = Logger.getLogger(CrosswordComposerSolver.class.getName());

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
                                        final Solution result) {
        return AdaptedSolverResult.success(puzzle.idToPosition(), result);
    }

    @Override
    public String name() {
        return "Crossword Composer";
    }

    @Override
    public String description() {
        return "The solver powering the Crossword Composer software. Written in Rust.";
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
        final Puzzle adaptedPuzzle = PuzzleAdapter.adapt(numberedPuzzle);

        final Optional<Solution> optResult = safeSolve(adaptedPuzzle, adaptedDictionary);

        return optResult.map(result -> success(numberedPuzzle, result))
                        .orElseGet(() -> impossible(numberedPuzzle));
    }

    /**
     * Solves the given puzzle using the given dictionary, catching any error raised by the solver.
     *
     * @param adaptedPuzzle     the puzzle to solve
     * @param adaptedDictionary the dictionary
     * @return the {@link Solution}, if any; {@link Optional#empty()} otherwise
     */
    private Optional<Solution> safeSolve(final Puzzle adaptedPuzzle,
                                         final com.gitlab.super7ramp.crosswords.solver.paulgb.Dictionary adaptedDictionary) {
        try {
            return solver.solve(adaptedPuzzle, adaptedDictionary);
        } catch (final SolverErrorException e) {
            LOGGER.log(Level.WARNING, "Crossword composer solver encountered an error.", e);
            return Optional.empty();
        }
    }
}
