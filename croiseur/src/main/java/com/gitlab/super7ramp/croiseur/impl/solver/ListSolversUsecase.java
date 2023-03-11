/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.solver;

import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverDescription;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparing;

/**
 * The list-solvers usecase.
 */
final class ListSolversUsecase {

    /**
     * Compares solver name by their name: Just the default order except if given name is the
     * preferred one which is always considered as the lowest (hence the first).
     */
    private static final class SolverNameComparator implements Comparator<String> {

        /** The preferred known solver. */
        private static final String PREFERRED_SOLVER = "Ginsberg";

        /**
         * Constructs an instance.
         */
        SolverNameComparator() {
            // Nothing to do.
        }

        @Override
        public int compare(final String a, final String b) {
            final int result;
            if (PREFERRED_SOLVER.equals(a)) {
                if (PREFERRED_SOLVER.equals(b)) {
                    result = 0;
                } else {
                    result = -1;
                }
            } else {
                if (PREFERRED_SOLVER.equals(b)) {
                    result = 1;
                } else {
                    result = a.compareTo(b);
                }
            }
            return result;
        }
    }

    /** The solvers indexed by their names. */
    private final List<SolverDescription> solvers;

    /** The presenter. */
    private final SolverPresenter presenter;

    /**
     * Constructs an instance.
     *
     * @param solversArg   the solvers
     * @param presenterArg the presenter
     */
    ListSolversUsecase(final Collection<CrosswordSolver> solversArg,
                       final SolverPresenter presenterArg) {
        solvers = solversArg.stream()
                            .map(solver -> new SolverDescription(solver.name(),
                                    solver.description()))
                            .sorted(comparing(SolverDescription::name, new SolverNameComparator()))
                            .toList();
        presenter = presenterArg;
    }

    /**
     * Processes the list solvers command.
     */
    void process() {
        if (solvers.isEmpty()) {
            presenter.presentSolverError("No solver found");
        } else {
            presenter.presentAvailableSolvers(solvers);
        }
    }
}
