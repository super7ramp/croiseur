package com.gitlab.super7ramp.crosswords.cli.presenter;

import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverDescription;
import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverInitialisationState;
import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverPresenter;
import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverProgress;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

import java.util.List;
import java.util.ResourceBundle;

/**
 * CLI implementation of {@link SolverPresenter}.
 */
final class CliSolverPresenter implements SolverPresenter {

    /** The translated strings. */
    private static final ResourceBundle L10N = ResourceBundle.getBundle("/l10n/SolverMessages");

    /** The message format. */
    private static final String PROGRESS_FORMAT = L10N.getString("progress.format") + "\r";

    /** Solver list format. */
    private static final String SOLVER_LIST_FORMAT = "%-16s\t%-54s%n";

    /** The best completion percentage reached. */
    private short bestCompletionPercentage;

    /**
     * Constructs an instance.
     */
    CliSolverPresenter() {
        // Nothing to do.
    }

    @Override
    public void presentAvailableSolvers(final List<SolverDescription> solverDescriptions) {
        System.out.printf(SOLVER_LIST_FORMAT, L10N.getString("name"), L10N.getString("description"));
        System.out.printf(SOLVER_LIST_FORMAT, "--------", "-----------");

        solverDescriptions.forEach(solver -> System.out.printf(SOLVER_LIST_FORMAT, solver.name(),
                solver.description()));
    }

    @Override
    public void presentSolverInitialisationState(final SolverInitialisationState solverInitialisationState) {
        if (solverInitialisationState == SolverInitialisationState.STARTED) {
            publishSolverInitialisationStarted();
        } else {
            publishSolverInitialisationStopped();
        }
    }

    @Override
    public void presentProgress(final SolverProgress solverProgress) {
        final short completionPercentage = solverProgress.completionPercentage();
        if (completionPercentage > bestCompletionPercentage) {
            bestCompletionPercentage = completionPercentage;
        }
        System.err.printf(PROGRESS_FORMAT, completionPercentage, bestCompletionPercentage);
    }

    @Override
    public void presentResult(final SolverResult result) {
        System.out.println(SolverResultFormatter.format(result));
    }

    @Override
    public void presentSolverError(String error) {
        System.err.println(error);
    }

    private void publishSolverInitialisationStarted() {
        System.out.println(L10N.getString("state.initializing"));
        bestCompletionPercentage = 0;
    }

    private void publishSolverInitialisationStopped() {
        System.out.println(L10N.getString("state.initialized"));
    }
}
