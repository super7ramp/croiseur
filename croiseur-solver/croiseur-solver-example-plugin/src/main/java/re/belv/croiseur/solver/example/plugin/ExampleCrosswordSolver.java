/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.example.plugin;

import re.belv.croiseur.common.puzzle.GridPosition;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.spi.solver.CrosswordSolver;
import re.belv.croiseur.spi.solver.Dictionary;
import re.belv.croiseur.spi.solver.ProgressListener;
import re.belv.croiseur.spi.solver.SolverResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An example crossword solver plugin.
 */
public final class ExampleCrosswordSolver implements CrosswordSolver {

    /**
     * Constructs an instance.
     */
    public ExampleCrosswordSolver() {
        /*
         * It's important that the constructor is public and without arguments: The service
         * loader mechanism used to load plugins requires it.
         */
    }

    @Override
    public String name() {
        return "Example";
    }

    @Override
    public String description() {
        return "An example crossword solver plugin. Always fails to solve puzzles.";
    }

    @Override
    public SolverResult solve(final PuzzleGrid puzzle, final Dictionary dictionary,
                              final ProgressListener progressListener) {

        /*
         * Here you can put the logic to solve the puzzle.
         *
         * You may want to call external libraries here to help you with that (and you're right,
         * it's not a trivial task).
         *
         * It is advised that you keep the code in plugin simple and focused to adapt the real
         * solver library API to the croiseur solver SPI.
         */

        /*
         * You may notify solver progress using the ProgressListener callback.
         *
         * If you're using an external library, then you'll need to adapt its progress
         * notification API to croiseur-solver-spi's ProgressListener.
         *
         * If the external library you use doesn't provide a progress notification mechanism,
         * then you just cannot provide it. That's OK: It's purely indicative.
         */
        progressListener.onInitialisationStart();
        progressListener.onInitialisationEnd();
        progressListener.onSolverProgressUpdate((short) 0);
        progressListener.onSolverProgressUpdate((short) 50);
        progressListener.onSolverProgressUpdate((short) 100);

        /*
         * When the solver has finished running, a result respecting the SolverResult interface
         * needs to be returned. Here's a dummy example as an anonymous class. You may want to
         * declare a separate named class in a real implementation.
         */
        return new SolverResult() {
            @Override
            public Kind kind() {
                /*
                 * This example plugin is unable to solve anything, so it always considers that no
                 * solution exists.
                 */
                return Kind.IMPOSSIBLE;
            }

            @Override
            public Map<GridPosition, Character> filledBoxes() {
                /*
                 * This example plugin is unable to solve anything. A normal solver would put the
                 * boxes it has filled here.
                 */
                return Collections.emptyMap();
            }

            @Override
            public Set<GridPosition> unsolvableBoxes() {
                /*
                 * When solver considers the puzzle as impossible, it can indicate the positions
                 * that it thinks are problematic. Here, solver creates a set of all grid
                 * positions because it doesn't know any better.
                 */
                final Set<GridPosition> unsolvableBoxes = new HashSet<>();
                for (int column = 0; column < puzzle.width(); column++) {
                    for (int row = 0; row < puzzle.height(); row++) {
                        unsolvableBoxes.add(new GridPosition(column, row));
                    }
                }
                return unsolvableBoxes;
            }
        };
    }
}
