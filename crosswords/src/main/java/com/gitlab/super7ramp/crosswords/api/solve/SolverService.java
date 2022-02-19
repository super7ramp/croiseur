package com.gitlab.super7ramp.crosswords.api.solve;

/**
 * Services pertaining to solving crossword puzzle.
 */
public interface SolverService {

    /**
     * Solve a puzzle.
     *
     * @param event details about the puzzle to solve
     */
    void solve(final SolveRequest event);
}
