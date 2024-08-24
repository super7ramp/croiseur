/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import re.belv.croiseur.solver.ginsberg.plugin.GinsbergCrosswordSolver;
import re.belv.croiseur.spi.solver.CrosswordSolver;

/** Solver provider adapting croiseur-solver-ginsberg. */
module re.belv.croiseur.solver.ginsberg.plugin {
    requires com.gitlab.super7ramp.croiseur.solver.ginsberg;
    requires transitive re.belv.croiseur.spi.solver;

    provides CrosswordSolver with
            GinsbergCrosswordSolver;
}
