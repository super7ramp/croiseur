package com.gitlab.super7ramp.crosswords.spi.presenter.solver;

/**
 * The solver progress.
 *
 * @param completionPercentage the completion percentage
 */
// TODO add intermediate solver result
public record SolverProgress(short completionPercentage) {

    /**
     * Validates fields.
     */
    public SolverProgress {
        if (completionPercentage < 0 || completionPercentage > 100) {
            throw new IllegalArgumentException("Expected percentage between 0 and 100, got " + completionPercentage);
        }
    }
}