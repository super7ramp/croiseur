/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import com.gitlab.super7ramp.croiseur.solver.ginsberg.plugin.GinsbergCrosswordSolver;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;

/**
 * Solver plugin module.
 */
module com.gitlab.super7ramp.croiseur.solver.ginsberg.plugin {
    requires com.gitlab.super7ramp.croiseur.solver.ginsberg;
    requires transitive com.gitlab.super7ramp.croiseur.spi.solver;
    provides CrosswordSolver with GinsbergCrosswordSolver;
}