package com.gitlab.super7ramp.crosswords.cli.solve.adapted;

import com.gitlab.super7ramp.crosswords.solver.api.ProgressListener;

/**
 * Implementation of {@link ProgressListener}.
 */
final class ProgressListenerImpl implements ProgressListener {

    /** The message format. */
    private static final String PROGRESS_FORMAT = "Completion: %3d %% [best: %3d %%]\r";

    /** The best completion percentage reached. */
    private short bestCompletionPercentage;

    /**
     * Constructor.
     */
    ProgressListenerImpl() {
        // Nothing to do.
    }

    @Override
    public void onSolverProgressUpdate(final short completionPercentage) {
        if (completionPercentage > bestCompletionPercentage) {
            bestCompletionPercentage = completionPercentage;
        }
        System.err.printf(PROGRESS_FORMAT, completionPercentage, bestCompletionPercentage);
    }
}