/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import com.gitlab.super7ramp.croiseur.solver.example.plugin.ExampleCrosswordSolver;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;

/**
 * Example solver plugin module
 */
module com.gitlab.super7ramp.croiseur.solver.example.plugin {
    requires com.gitlab.super7ramp.croiseur.spi.solver;
    provides CrosswordSolver with ExampleCrosswordSolver;
}