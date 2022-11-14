package com.gitlab.super7ramp.crosswords.solver.ginsberg.plugin;

import com.gitlab.super7ramp.crosswords.common.GridPosition;
import com.gitlab.super7ramp.crosswords.common.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.spi.solver.CrosswordSolver;
import com.gitlab.super7ramp.crosswords.spi.solver.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.solver.ProgressListener;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

import java.util.Map;
import java.util.Set;

/**
 * Implementation of {@link CrosswordSolver} adapting
 * {@link com.gitlab.super7ramp.crosswords.solver.ginsberg.GinsbergCrosswordSolver}.
 */
public final class GinsbergCrosswordSolver implements CrosswordSolver {

    /**
     * Adapts a {@link ProgressListener} to
     * {@link com.gitlab.super7ramp.crosswords.solver.ginsberg.ProgressListener}.
     */
    private static class AdaptedProgressListener implements com.gitlab.super7ramp.crosswords.solver.ginsberg.ProgressListener {

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
        public void onSolverProgressUpdate(short completionPercentage) {
            adapted.onSolverProgressUpdate(completionPercentage);
        }
    }

    /**
     * Adapts a {@link com.gitlab.super7ramp.crosswords.solver.ginsberg.SolverResult} to
     * {@link SolverResult}.
     */
    private static class AdaptedSolverResult implements SolverResult {

        /** The adapted instance. */
        private final com.gitlab.super7ramp.crosswords.solver.ginsberg.SolverResult adapted;

        /**
         * Constructs an instance.
         *
         * @param adaptedArg the adapted instance
         */
        AdaptedSolverResult(com.gitlab.super7ramp.crosswords.solver.ginsberg.SolverResult adaptedArg) {
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

    /** The adapted solver. */
    private final com.gitlab.super7ramp.crosswords.solver.ginsberg.GinsbergCrosswordSolver adapted;

    /**
     * Constructs an instance.
     */
    public GinsbergCrosswordSolver() {
        adapted = new com.gitlab.super7ramp.crosswords.solver.ginsberg.GinsbergCrosswordSolver();
    }

    @Override
    public SolverResult solve(final PuzzleDefinition puzzle, final Dictionary dictionary,
                              final ProgressListener progressListener) throws InterruptedException {
        final com.gitlab.super7ramp.crosswords.solver.ginsberg.Dictionary adaptedDictionary =
                dictionary::lookup;
        final com.gitlab.super7ramp.crosswords.solver.ginsberg.ProgressListener adaptedProgressListener
                = new AdaptedProgressListener(progressListener);
        final com.gitlab.super7ramp.crosswords.solver.ginsberg.SolverResult result =
                adapted.solve(puzzle, adaptedDictionary, adaptedProgressListener);
        return new AdaptedSolverResult(result);
    }
}
