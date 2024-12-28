/*
 * SPDX-FileCopyrightText: 2024 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.benchmark;

import org.openjdk.jmh.annotations.*;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.spi.solver.CrosswordSolver;
import re.belv.croiseur.spi.solver.Dictionary;

/** Benchmark for crossword solvers implementing the {@link CrosswordSolver} interface. */
@State(Scope.Benchmark)
public abstract class CrosswordSolverBenchmark {

    /** The injected benchmark puzzle. */
    private PuzzleGrid puzzle;

    /** The injected benchmark dictionary. */
    private Dictionary dictionary;

    /**
     * Sets up the benchmark.
     *
     * <p>This method is not meant to be overridden, it is public and not final only for the JMH instrumentation to
     * work.
     *
     * @param benchPuzzle the injected benchmark puzzle
     * @param benchDictionary the injected benchmark dictionary
     */
    @Setup
    public void setup(final BenchPuzzle benchPuzzle, final BenchDictionary benchDictionary) {
        puzzle = benchPuzzle.get();
        dictionary = benchDictionary.get();
    }

    /**
     * Benchmarks the solver.
     *
     * @throws InterruptedException if interrupted while solving
     */
    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    public final void solve() throws InterruptedException {
        solver().solve(puzzle, dictionary);
    }

    /**
     * Returns the {@link CrosswordSolver} implementation to benchmark.
     *
     * @return the {@link CrosswordSolver} implementation to benchmark.
     */
    protected abstract CrosswordSolver solver();
}
