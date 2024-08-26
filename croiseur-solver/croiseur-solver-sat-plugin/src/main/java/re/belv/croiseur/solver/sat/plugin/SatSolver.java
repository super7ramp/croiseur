/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.sat.plugin;

import static java.util.stream.Collectors.toSet;
import static re.belv.croiseur.common.puzzle.GridPosition.at;

import java.util.*;
import re.belv.croiseur.common.puzzle.GridPosition;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.solver.sat.Pos;
import re.belv.croiseur.solver.sat.Solver;
import re.belv.croiseur.spi.solver.CrosswordSolver;
import re.belv.croiseur.spi.solver.Dictionary;
import re.belv.croiseur.spi.solver.ProgressListener;
import re.belv.croiseur.spi.solver.SolverResult;

/** Implementation of {@link CrosswordSolver} adapting croiseur-solver-sat's solver. */
public final class SatSolver implements CrosswordSolver {

    /** Adapts the raw SAT solver result to {@link SolverResult}. */
    private static final class AdaptedSolverResult implements SolverResult {

        /** The raw result. */
        private final Solver.Result result;

        /**
         * Constructs an instance.
         *
         * @param solverResultArg the solver result
         */
        AdaptedSolverResult(final Solver.Result solverResultArg) {
            result = solverResultArg;
        }

        @Override
        public Kind kind() {
            return result instanceof Solver.Result.Sat ? Kind.SUCCESS : Kind.IMPOSSIBLE;
        }

        @Override
        public Map<GridPosition, Character> filledBoxes() {
            return switch (result) {
                case Solver.Result.Unsat ignored -> Collections.emptyMap();
                case Solver.Result.Sat(final char[][] grid) -> {
                    final var filledBoxes = new HashMap<GridPosition, Character>();
                    for (int row = 0; row < grid.length; row++) {
                        for (int column = 0; column < grid[row].length; column++) {
                            final char value = grid[row][column];
                            if (value != '#') {
                                filledBoxes.put(at(column, row), value);
                            }
                        }
                    }
                    yield filledBoxes;
                }
            };
        }

        @Override
        public Set<GridPosition> unsolvableBoxes() {
            return switch (result) {
                case Solver.Result.Sat ignored -> Collections.emptySet();
                case Solver.Result.Unsat(final Set<Pos> nonAssignablePositions) ->
                        nonAssignablePositions.stream().map(pos -> at(pos.column(), pos.row())).collect(toSet());
            };
        }
    }

    /** Constructs an instance. */
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

        final Solver.Result result = solver.solve();
        progressListener.onSolverProgressUpdate((short) 100);

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
     * Filters dictionary to eliminate words containing characters outside A-Z range, since solver only accepts this
     * very narrow range.
     *
     * <p>Performance-wise, it's bad since it does yet another expensive copy of the dictionary but unless the solver
     * covers a wider character range or the input dictionary is more aggressively filtered upstream, there is not much
     * that can be done here.
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
