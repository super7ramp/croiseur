/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.presenter.solver;

/**
 * A solver's description.
 *
 * @param name        the solver's unique name
 * @param description the solver description
 */
public record SolverDescription(String name, String description) {
    // Nothing to add
}
