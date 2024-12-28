/*
 * SPDX-FileCopyrightText: 2024 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.plugin.benchmark;

import re.belv.croiseur.solver.benchmark.CrosswordSolverBenchmark;
import re.belv.croiseur.solver.ginsberg.plugin.GinsbergCrosswordSolver;
import re.belv.croiseur.spi.solver.CrosswordSolver;

/** Benchmark for {@link GinsbergCrosswordSolver}. */
public class GinsbergCrosswordSolverBenchmark extends CrosswordSolverBenchmark {
    @Override
    protected final CrosswordSolver solver() {
        return new GinsbergCrosswordSolver();
    }
}
