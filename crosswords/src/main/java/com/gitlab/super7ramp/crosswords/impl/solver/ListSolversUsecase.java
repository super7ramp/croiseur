/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.impl.solver;

import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverDescription;
import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverPresenter;
import com.gitlab.super7ramp.crosswords.spi.solver.CrosswordSolver;

import java.util.Collection;
import java.util.List;

/**
 * The list-solvers usecase.
 */
final class ListSolversUsecase {

    /** The solvers indexed by their names. */
    private final List<SolverDescription> solvers;

    /** The presenter. */
    private final SolverPresenter presenter;

    /**
     * Constructs an instance.
     *
     * @param solversArg the solvers
     * @param presenterArg the presenter
     */
    ListSolversUsecase(final Collection<CrosswordSolver> solversArg,
                       final SolverPresenter presenterArg) {
        solvers = solversArg.stream().map(solver -> new SolverDescription(solver.name(),
                solver.description())).toList();
        presenter = presenterArg;
    }

    /**
     * Processes the list solvers command.
     */
    void process() {
        presenter.presentAvailableSolvers(solvers);
    }
}
