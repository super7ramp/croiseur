/*
 * SPDX-FileCopyrightText: 2024 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.sat.plugin.benchmark;

import re.belv.croiseur.solver.benchmark.CrosswordSolverBenchmark;
import re.belv.croiseur.solver.sat.plugin.SatSolver;
import re.belv.croiseur.spi.solver.CrosswordSolver;

/** Benchmark for {@link SatSolver}. */
public class SatSolverBenchmark extends CrosswordSolverBenchmark {
    @Override
    protected final CrosswordSolver solver() {
        return new SatSolver();
    }
}
