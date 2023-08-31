/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.session.model;

import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverDescription;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

/**
 * The solver session model.
 */
@Component
@SessionScope
public class SolverSessionModel {

    /** The list of solvers for this session. */
    private final List<SolverDescription> solvers;

    /**
     * Constructs an instance.
     */
    public SolverSessionModel() {
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
     * Sets the list of solvers for this session.
     *
     * @param solverDescriptions the list of solverDescriptions
     */
    public void solvers(final List<SolverDescription> solverDescriptions) {
        solvers.clear();
        solvers.addAll(solverDescriptions);
    }
}
