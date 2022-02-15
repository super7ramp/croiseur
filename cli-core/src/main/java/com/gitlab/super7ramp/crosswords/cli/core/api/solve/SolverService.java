package com.gitlab.super7ramp.crosswords.cli.core.api.solve;

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
