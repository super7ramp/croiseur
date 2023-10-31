/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import re.belv.croiseur.solver.sat.plugin.SatSolver;
import re.belv.croiseur.spi.solver.CrosswordSolver;

/**
 * Solver provider adapting croiseur-solver-sat.
 */
module re.belv.croiseur.solver.sat.plugin {
    requires re.belv.croiseur.solver.sat;
    requires re.belv.croiseur.spi.solver;
    provides CrosswordSolver with SatSolver;
}