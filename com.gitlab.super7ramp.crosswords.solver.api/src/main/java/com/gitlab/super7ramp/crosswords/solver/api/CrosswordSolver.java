package com.gitlab.super7ramp.crosswords.solver.api;

/**
 *
 */
public interface CrosswordSolver {

    SolverResult solve(final CrosswordPuzzle puzzle, final Dictionary dictionary) throws InterruptedException;

}
