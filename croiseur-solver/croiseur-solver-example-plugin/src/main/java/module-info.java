/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import re.belv.croiseur.solver.example.plugin.ExampleCrosswordSolver;
import re.belv.croiseur.spi.solver.CrosswordSolver;

/**
 * Example solver plugin module
 */
module re.belv.croiseur.solver.example.plugin {
    requires re.belv.croiseur.spi.solver;

    provides CrosswordSolver with
            ExampleCrosswordSolver;
}
