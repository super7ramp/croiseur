/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.sat.plugin;

import static re.belv.croiseur.common.puzzle.GridPosition.at;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import re.belv.croiseur.common.puzzle.GridPosition;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.solver.sat.Solver;
import re.belv.croiseur.spi.solver.CrosswordSolver;
import re.belv.croiseur.spi.solver.Dictionary;
import re.belv.croiseur.spi.solver.ProgressListener;
import re.belv.croiseur.spi.solver.SolverResult;

/**
 * Implementation of {@link CrosswordSolver} adapting croiseur-solver-sat's solver.
 */
public final class SatSolver implements CrosswordSolver {

    /**
     * Adapts the raw SAT solver result to {@link SolverResult}.
     */
    private static final class AdaptedSolverResult implements SolverResult {

        /** The raw result. */
        private final char[][] outputGrid;

        /** The input grid row count. */
        private final int inputGridRowCount;

        /** The output grid row count. */
        private final int inputGridColumnCount;

        /**
         * Constructs an instance.
         *
         * @param outputGridArg the output grid
         * @param inputGrid the input grid
         */
        AdaptedSolverResult(final char[][] outputGridArg, final char[][] inputGrid) {
            outputGrid = outputGridArg;
            inputGridRowCount = inputGrid.length;
            inputGridColumnCount = inputGridRowCount > 0 ? inputGrid[0].length : 0;
        }

        @Override
        public Kind kind() {
            return outputGrid.length == 0 && inputGridRowCount != 0 ? Kind.IMPOSSIBLE : Kind.SUCCESS;
        }

        @Override
        public Map<GridPosition, Character> filledBoxes() {
            final Map<GridPosition, Character> filledBoxes = new HashMap<>();
            for (int row = 0; row < outputGrid.length; row++) {
                for (int column = 0; column < outputGrid[row].length; column++) {
                    final char value = outputGrid[row][column];
                    if (value != '#') {
                        filledBoxes.put(at(column, row), value);
                    }
                }
            }
            return filledBoxes;
        }

        @Override
        public Set<GridPosition> unsolvableBoxes() {
            if (kind() == Kind.SUCCESS) {
                return Collections.emptySet();
            }
            // Return all boxes, since solver API does not expose found conflicts in case of
            // non-satisfiability.
            final Set<GridPosition> allBoxes = new HashSet<>();
            for (int row = 0; row < inputGridRowCount; row++) {
                for (int column = 0; column < inputGridColumnCount; column++) {
                    allBoxes.add(at(column, row));
                }
            }
            return allBoxes;
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
        return ResourceBundle.getBundle("re.belv.croiseur.solver.sat.plugin.Messages")
                .getString("description");
    }

    @Override
    public SolverResult solve(
            final PuzzleGrid puzzle, final Dictionary dictionary, final ProgressListener progressListener)
            throws InterruptedException {

        progressListener.onInitialisationStart();
        final char[][] inputGrid = convertToArray(puzzle);
        final String[] words = filterAndConvertToArray(dictionary);
        final Solver solver = new Solver(inputGrid, words);
        progressListener.onInitialisationEnd();

        final char[][] outputGrid = solver.solve();
        progressListener.onSolverProgressUpdate((short) 100);

        return new AdaptedSolverResult(outputGrid, inputGrid);
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
