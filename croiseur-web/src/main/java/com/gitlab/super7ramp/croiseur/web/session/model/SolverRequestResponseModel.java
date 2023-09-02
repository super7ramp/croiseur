/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.session.model;

import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverDescription;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.List;

/**
 * The solver request response model: All data relative to the solver service and relevant only for
 * the lifetime of a request.
 */
@Component
@RequestScope
public class SolverRequestResponseModel {

    /** The list of solvers for this session. */
    private final List<SolverDescription> solvers;

    /**
     * Constructs an instance.
     */
    public SolverRequestResponseModel() {
        solvers = new ArrayList<>();
    }

    /**
     * Retrieves the list of solvers.
     *
     * @return the solvers
     */
    public Iterable<SolverDescription> solvers() {
        return solvers;
    }

    /**
     * Sets the list of solvers.
     *
     * @param solverDescriptions the list of solvers
     */
    public void solvers(final List<SolverDescription> solverDescriptions) {
        solvers.addAll(solverDescriptions);
    }
}
