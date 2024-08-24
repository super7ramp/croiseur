/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.plugin;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import re.belv.croiseur.common.puzzle.GridPosition;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.spi.solver.CrosswordSolver;
import re.belv.croiseur.spi.solver.Dictionary;
import re.belv.croiseur.spi.solver.ProgressListener;
import re.belv.croiseur.spi.solver.SolverResult;

/**
 * Implementation of {@link CrosswordSolver} adapting {@link re.belv.croiseur.solver.ginsberg.GinsbergCrosswordSolver}.
 */
public final class GinsbergCrosswordSolver implements CrosswordSolver {

    /** Adapts a {@link ProgressListener} to {@link re.belv.croiseur.solver.ginsberg.ProgressListener}. */
    private static final class AdaptedProgressListener implements re.belv.croiseur.solver.ginsberg.ProgressListener {

        /** The adapted listener. */
        private final ProgressListener adapted;

        /**
         * Constructs an instance.
         *
         * @param adaptedArg the instance to adapt
         */
        AdaptedProgressListener(final ProgressListener adaptedArg) {
            adapted = adaptedArg;
        }

        @Override
        public void onInitialisationEnd() {
            adapted.onInitialisationEnd();
        }

        @Override
        public void onInitialisationStart() {
            adapted.onInitialisationStart();
        }

        @Override
        public void onSolverProgressUpdate(final short completionPercentage) {
            adapted.onSolverProgressUpdate(completionPercentage);
        }
    }

    /** Adapts a {@link re.belv.croiseur.solver.ginsberg.SolverResult} to {@link SolverResult}. */
    private static final class AdaptedSolverResult implements SolverResult {

        /** The adapted instance. */
        private final re.belv.croiseur.solver.ginsberg.SolverResult adapted;

        /**
         * Constructs an instance.
         *
         * @param adaptedArg the adapted instance
         */
        AdaptedSolverResult(final re.belv.croiseur.solver.ginsberg.SolverResult adaptedArg) {
            adapted = adaptedArg;
        }

        @Override
        public Kind kind() {
            return Kind.valueOf(adapted.kind().name());
        }

        @Override
        public Map<GridPosition, Character> filledBoxes() {
            return adapted.filledBoxes();
        }

        @Override
        public Set<GridPosition> unsolvableBoxes() {
            return adapted.unsolvableBoxes();
        }

        @Override
        public String toString() {
            return "AdaptedSolverResult{adapted=" + adapted + '}';
        }
    }

    /** This solver's name. */
    private static final String NAME = "Ginsberg";

    /** The adapted solver. */
    private final re.belv.croiseur.solver.ginsberg.GinsbergCrosswordSolver adapted;

    /** Constructs an instance. */
    public GinsbergCrosswordSolver() {
        adapted = new re.belv.croiseur.solver.ginsberg.GinsbergCrosswordSolver();
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String description() {
        return ResourceBundle.getBundle("re.belv.croiseur.solver.ginsberg.plugin.Messages")
                .getString("description");
    }

    @Override
    public SolverResult solve(
            final PuzzleGrid puzzle, final Dictionary dictionary, final ProgressListener progressListener)
            throws InterruptedException {
        final re.belv.croiseur.solver.ginsberg.Dictionary adaptedDictionary = dictionary::words;
        final re.belv.croiseur.solver.ginsberg.ProgressListener adaptedProgressListener =
                new AdaptedProgressListener(progressListener);
        final re.belv.croiseur.solver.ginsberg.SolverResult result =
                adapted.solve(puzzle, adaptedDictionary, adaptedProgressListener);
        return new AdaptedSolverResult(result);
    }
}
