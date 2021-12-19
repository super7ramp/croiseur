package com.gitlab.super7ramp.crosswords.solver.spi;

import com.gitlab.super7ramp.crosswords.solver.api.CrosswordSolver;

/**
 * Crossword Solver Provider.
 */
public interface CrosswordSolverProvider {

    /**
     * Return a new instance of solver.
     *
     * @return a new instance of solver
     */
    CrosswordSolver solver();
}
