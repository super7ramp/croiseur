package com.gitlab.super7ramp.crosswords.solver.api.spi;

import com.gitlab.super7ramp.crosswords.solver.api.CrosswordSolver;

/**
 * Crossword Solver Provider.
 */
public interface CrosswordSolverProvider {

    CrosswordSolver solver();
}
