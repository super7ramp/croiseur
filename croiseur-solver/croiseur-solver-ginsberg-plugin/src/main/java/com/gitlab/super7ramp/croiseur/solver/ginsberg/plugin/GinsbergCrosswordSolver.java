/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.plugin;

import com.gitlab.super7ramp.croiseur.common.GridPosition;
import com.gitlab.super7ramp.croiseur.common.PuzzleDefinition;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;
import com.gitlab.super7ramp.croiseur.spi.solver.Dictionary;
import com.gitlab.super7ramp.croiseur.spi.solver.ProgressListener;
import com.gitlab.super7ramp.croiseur.spi.solver.SolverResult;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Implementation of {@link CrosswordSolver} adapting
 * {@link com.gitlab.super7ramp.croiseur.solver.ginsberg.GinsbergCrosswordSolver}.
 */
public final class GinsbergCrosswordSolver implements CrosswordSolver {

    /**
     * Adapts a {@link ProgressListener} to
     * {@link com.gitlab.super7ramp.croiseur.solver.ginsberg.ProgressListener}.
     */
    private static final class AdaptedProgressListener implements com.gitlab.super7ramp.croiseur.solver.ginsberg.ProgressListener {

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

    /**
     * Adapts a {@link com.gitlab.super7ramp.croiseur.solver.ginsberg.SolverResult} to
     * {@link SolverResult}.
     */
    private static final class AdaptedSolverResult implements SolverResult {

        /** The adapted instance. */
        private final com.gitlab.super7ramp.croiseur.solver.ginsberg.SolverResult adapted;

        /**
         * Constructs an instance.
         *
         * @param adaptedArg the adapted instance
         */
        AdaptedSolverResult(final com.gitlab.super7ramp.croiseur.solver.ginsberg.SolverResult adaptedArg) {
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

    /** This solver's description. */
    private static final String DESCRIPTION = ResourceBundle
            .getBundle("com.gitlab.super7ramp.croiseur.solver.ginsberg.plugin.Messages")
            .getString("description");

    /** The adapted solver. */
    private final com.gitlab.super7ramp.croiseur.solver.ginsberg.GinsbergCrosswordSolver adapted;

    /**
     * Constructs an instance.
     */
    public GinsbergCrosswordSolver() {
        adapted = new com.gitlab.super7ramp.croiseur.solver.ginsberg.GinsbergCrosswordSolver();
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public SolverResult solve(final PuzzleDefinition puzzle, final Dictionary dictionary,
                              final ProgressListener progressListener) throws InterruptedException {
        final com.gitlab.super7ramp.croiseur.solver.ginsberg.Dictionary adaptedDictionary =
                dictionary::lookup;
        final com.gitlab.super7ramp.croiseur.solver.ginsberg.ProgressListener adaptedProgressListener = new AdaptedProgressListener(progressListener);
        final com.gitlab.super7ramp.croiseur.solver.ginsberg.SolverResult result =
                adapted.solve(puzzle, adaptedDictionary, adaptedProgressListener);
        return new AdaptedSolverResult(result);
    }
}
