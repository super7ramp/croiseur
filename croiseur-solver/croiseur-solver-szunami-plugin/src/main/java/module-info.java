/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import com.gitlab.super7ramp.croiseur.solver.szunami.plugin.SzunamiSolver;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;

/**
 * Solver plugin module.
 */
module com.gitlab.super7ramp.croiseur.solver.szunami.plugin {
    requires com.gitlab.super7ramp.croiseur.solver.szunami;
    requires com.gitlab.super7ramp.croiseur.spi.solver;
    provides CrosswordSolver with SzunamiSolver;

    requires java.logging;
}