/*
 * SPDX-FileCopyrightText: 2024 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.benchmark;

import org.openjdk.jmh.annotations.*;
import re.belv.croiseur.solver.ginsberg.GinsbergCrosswordSolver;

@State(Scope.Benchmark)
public class SolverBenchmark {

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    public void solve(final PuzzleGridProvider puzzleGridProvider, final DictionaryProvider dictionary)
            throws InterruptedException {
        solver().solve(puzzleGridProvider.get(), dictionary.get());
    }

    private GinsbergCrosswordSolver solver() {
        return new GinsbergCrosswordSolver();
    }
}
