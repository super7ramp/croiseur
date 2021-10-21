package com.gitlab.super7ramp.crosswords.solver.lib.core;

import java.util.Optional;

/**
 * Encapsulates the heuristics for selecting the value of a variable.
 */
public interface CandidateChooser {

    /**
     * Choose a candidate for the given variable.
     *
     * @param wordVariable word variable to choose a candidate for
     * @return the appropriate candidate, or {@link Optional#empty()} if no suitable candidate can be found
     */
    Optional<String> find(final Slot wordVariable);
}
