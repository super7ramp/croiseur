package com.gitlab.super7ramp.crosswords.spi.presenter.solver;

/**
 * A solver's description.
 *
 * @param name the solver's unique name
 * @param description the solver description
 */
public record SolverDescription(String name, String description) {
    // Nothing to add
}
