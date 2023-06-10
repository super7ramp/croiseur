/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.paulgb.plugin;

import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import com.gitlab.super7ramp.croiseur.solver.paulgb.Puzzle;
import com.gitlab.super7ramp.croiseur.solver.paulgb.Solution;
import com.gitlab.super7ramp.croiseur.solver.paulgb.Solver;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;
import com.gitlab.super7ramp.croiseur.spi.solver.Dictionary;
import com.gitlab.super7ramp.croiseur.spi.solver.ProgressListener;
import com.gitlab.super7ramp.croiseur.spi.solver.SolverResult;

import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Implementation of {@link CrosswordSolver} adapting {@link Solver paulgb's Crossword Composer}
 * solver}.
 */
public final class CrosswordComposerSolver implements CrosswordSolver {

    /** The solver name. */
    private static final String NAME = "Crossword Composer";

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
        return NAME;
    }

    @Override
    public String description() {
        return ResourceBundle
                .getBundle("com.gitlab.super7ramp.croiseur.solver.paulgb.plugin.Messages")
                .getString("description");
    }

    @Override
    public SolverResult solve(final PuzzleGrid puzzle, final Dictionary dictionary,
                              final ProgressListener progressListener) throws InterruptedException {

        final NumberedPuzzleDefinition numberedPuzzle = new NumberedPuzzleDefinition(puzzle);
        if (!numberedPuzzle.filled().isEmpty()) {
            throw new UnsupportedOperationException(
                    "Crossword Composer solver does not support pre-filled grids");
        }
        final com.gitlab.super7ramp.croiseur.solver.paulgb.Dictionary adaptedDictionary =
                DictionaryAdapter.adapt(dictionary);
        final Puzzle adaptedPuzzle = PuzzleAdapter.adapt(numberedPuzzle);

        final Optional<Solution> optResult = solver.solve(adaptedPuzzle, adaptedDictionary);

        return optResult.map(result -> success(numberedPuzzle, result))
                        .orElseGet(() -> impossible(numberedPuzzle));
    }

}
