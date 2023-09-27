/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.sat.plugin;

import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import com.gitlab.super7ramp.croiseur.solver.sat.Solver;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;
import com.gitlab.super7ramp.croiseur.spi.solver.Dictionary;
import com.gitlab.super7ramp.croiseur.spi.solver.ProgressListener;
import com.gitlab.super7ramp.croiseur.spi.solver.SolverResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import static com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition.at;

/**
 * Implementation of {@link CrosswordSolver} adapting croiseur-solver-sat's solver.
 */
public final class SatSolver implements CrosswordSolver {

    /**
     * Adapts the raw SAT solver result to {@link SolverResult}.
     */
    private static final class AdaptedSolverResult implements SolverResult {

        /** The raw result. */
        private final char[][] cells;

        /**
         * Constructs an instance.
         *
         * @param cellsArg the raw result
         */
        AdaptedSolverResult(final char[][] cellsArg) {
            cells = cellsArg;
        }

        @Override
        public Kind kind() {
            return cells.length == 0 ? Kind.IMPOSSIBLE : Kind.SUCCESS;
        }

        @Override
        public Map<GridPosition, Character> filledBoxes() {
            final Map<GridPosition, Character> filledBoxes = new HashMap<>();
            for (int row = 0; row < cells.length; row++) {
                for (int column = 0; column < cells[row].length; column++) {
                    final char value = cells[row][column];
                    if (value != '#') {
                        filledBoxes.put(at(column, row), value);
                    }
                }
            }
            return filledBoxes;
        }

        @Override
        public Set<GridPosition> unsolvableBoxes() {
            // SAT solver API does not expose found conflict in case of non-satisfiability
            return Collections.emptySet();
        }
    }

    /**
     * Constructs an instance.
     */
    public SatSolver() {
        // Nothing to do.
    }

    @Override
    public String name() {
        return "SAT";
    }

    @Override
    public String description() {
        return ResourceBundle
                .getBundle("com.gitlab.super7ramp.croiseur.solver.sat.plugin.Messages")
                .getString("description");
    }

    @Override
    // TODO interruption?
    public SolverResult solve(final PuzzleGrid puzzle, final Dictionary dictionary,
                              final ProgressListener progressListener) {

        progressListener.onInitialisationStart();
        final char[][] grid = convertToArray(puzzle);
        final String[] words = filterAndConvertToArray(dictionary);
        final Solver solver = new Solver(grid, words);
        progressListener.onInitialisationEnd();

        final char[][] result = solver.solve();

        return new AdaptedSolverResult(result);
    }

    /**
     * Converts the {@link PuzzleGrid} to a raw char array.
     *
     * @param original the original puzzle grid
     * @return the puzzle grid as a raw char array
     */
    private static char[][] convertToArray(final PuzzleGrid original) {
        final char[][] adapted = new char[original.height()][original.width()];
        for (int row = 0; row < original.height(); row++) {
            for (int column = 0; column < original.width(); column++) {
                final GridPosition pos = at(column, row);
                if (original.shaded().contains(pos)) {
                    adapted[row][column] = '#';
                } else {
                    adapted[row][column] = original.filled().getOrDefault(pos, '.');
                }
            }
        }
        return adapted;
    }

    /**
     * Filters dictionary to eliminate words containing characters outside A-Z range, since solver
     * only accepts this very narrow range.
     * <p>
     * Performance-wise, it's bad since it does yet another expensive copy of the dictionary but
     * unless the solver covers a wider character range or the input dictionary is more aggressively
     * filtered upstream, there is not much that can be done here.
     *
     * @param dictionary the dictionary to filter
     * @return the filtered dictionary as a new raw array
     */
    private static String[] filterAndConvertToArray(final Dictionary dictionary) {
        return dictionary.words().stream()
                         .filter(word -> word.chars().allMatch(c -> c >= 'A' && c <= 'Z'))
                         .toArray(String[]::new);
    }
}
