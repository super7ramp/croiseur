/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import re.belv.croiseur.solver.szunami.plugin.SzunamiSolver;
import re.belv.croiseur.spi.solver.CrosswordSolver;

/**
 * Solver provider adapting croiseur-solver-szunami.
 */
module re.belv.croiseur.solver.szunami.plugin {
    requires re.belv.croiseur.solver.szunami;
    requires re.belv.croiseur.spi.solver;
    provides CrosswordSolver with SzunamiSolver;
}