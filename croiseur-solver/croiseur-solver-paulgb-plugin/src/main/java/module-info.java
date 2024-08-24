/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import re.belv.croiseur.solver.paulgb.plugin.CrosswordComposerSolver;
import re.belv.croiseur.spi.solver.CrosswordSolver;

/**
 * Solver provider adapting croiseur-solver-paulgb.
 */
module re.belv.croiseur.solver.paulgb.plugin {
    requires re.belv.croiseur.solver.paulgb;
    requires re.belv.croiseur.spi.solver;

    provides CrosswordSolver with
            CrosswordComposerSolver;
}
