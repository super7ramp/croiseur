package com.gitlab.super7ramp.crosswords.cli.presenter;

import com.gitlab.super7ramp.crosswords.spi.presenter.SolverInitialisationState;
import com.gitlab.super7ramp.crosswords.spi.presenter.SolverPresenter;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

/**
 * CLI implementation of {@link SolverPresenter}.
 */
final class CliSolverPresenter implements SolverPresenter {

    /** The message format. */
    private static final String PROGRESS_FORMAT = "Completion: %3d %% [best: %3d %%]\r";

    /** The best completion percentage reached. */
    private short bestCompletionPercentage;

    /**
     * Constructs an instance.
     */
    CliSolverPresenter() {
        // Nothing to do.
    }

    @Override
    public void presentSolverInitialisationState(SolverInitialisationState solverInitialisationState) {
        if (solverInitialisationState == SolverInitialisationState.STARTED) {
            publishSolverInitialisationStarted();
        } else {
            publishSolverInitialisationStopped();
        }
    }

    @Override
    public void presentProgress(final short completionPercentage) {
        if (completionPercentage > bestCompletionPercentage) {
            bestCompletionPercentage = completionPercentage;
        }
        System.err.printf(PROGRESS_FORMAT, completionPercentage, bestCompletionPercentage);
    }

    @Override
    public void presentResult(SolverResult result) {
        System.out.println(result);
    }

    @Override
    public void presentError(String error) {
        System.err.println(error);
    }

    private void publishSolverInitialisationStarted() {
        System.out.println("Initializing solver");
        bestCompletionPercentage = 0;
    }

    private void publishSolverInitialisationStopped() {
        System.out.println("Solver initialized");
    }
}
