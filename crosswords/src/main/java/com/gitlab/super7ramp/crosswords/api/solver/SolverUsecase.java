package com.gitlab.super7ramp.crosswords.api.solver;

/**
 * Services pertaining to solving crossword puzzle.
 */
public interface SolverUsecase {

    /**
     * Solve a puzzle.
     *
     * @param event details about the puzzle to solve
     */
    void solve(final SolveRequest event);
}
