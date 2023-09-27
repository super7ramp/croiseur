/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import com.gitlab.super7ramp.croiseur.solver.sat.plugin.SatSolver;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;


/**
 * Solver provider adapting croiseur-solver-sat.
 */
module com.gitlab.super7ramp.croiseur.solver.sat.plugin {
    requires com.gitlab.super7ramp.croiseur.solver.sat;
    requires com.gitlab.super7ramp.croiseur.spi.solver;
    provides CrosswordSolver with SatSolver;
}