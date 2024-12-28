/*
 * SPDX-FileCopyrightText: 2024 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.paulgb.plugin.benchmark;

import re.belv.croiseur.solver.benchmark.CrosswordSolverBenchmark;
import re.belv.croiseur.solver.paulgb.plugin.CrosswordComposerSolver;
import re.belv.croiseur.spi.solver.CrosswordSolver;

/** Benchmark for {@link CrosswordComposerSolver}. */
public class CrosswordComposerSolverBenchmark extends CrosswordSolverBenchmark {
    @Override
    protected final CrosswordSolver solver() {
        return new CrosswordComposerSolver();
    }
}
