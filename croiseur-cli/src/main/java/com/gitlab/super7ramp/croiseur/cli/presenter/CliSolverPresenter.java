/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.presenter;

import com.gitlab.super7ramp.croiseur.cli.l10n.ResourceBundles;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverDescription;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverInitialisationState;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverProgress;
import com.gitlab.super7ramp.croiseur.spi.solver.SolverResult;

import java.util.List;

import static com.gitlab.super7ramp.croiseur.cli.presenter.CliPresenterUtil.lineOf;

/**
 * CLI implementation of {@link SolverPresenter}.
 */
final class CliSolverPresenter implements SolverPresenter {

    /** The message format. */
    private static final String PROGRESS_FORMAT = $("progress.format") + '\r';

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

    /**
     * Returns the localised message with given key.
     *
     * @param key the message key
     * @return the localised message
     */
    private static String $(final String key) {
        return ResourceBundles.$("presenter.solver." + key);
    }

    @Override
    public void presentAvailableSolvers(final List<SolverDescription> solverDescriptions) {
        final String nameHeader = $("name");
        final String descriptionHeader = $("description");

        System.out.printf(SOLVER_LIST_FORMAT, nameHeader, descriptionHeader);
        System.out.printf(SOLVER_LIST_FORMAT, lineOf(nameHeader.length()),
                lineOf(descriptionHeader.length()));

        solverDescriptions.forEach(solver ->
                System.out.printf(SOLVER_LIST_FORMAT, solver.name(), solver.description()));
    }

    @Override
    public void presentSolverInitialisationState(final SolverInitialisationState solverInitialisationState) {
        if (solverInitialisationState == SolverInitialisationState.STARTED) {
            System.out.println($("state.initializing"));
            bestCompletionPercentage = 0;
        } else {
            System.out.println($("state.initialized"));
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
    public void presentSolverError(final String error) {
        System.err.println(error);
    }

}
