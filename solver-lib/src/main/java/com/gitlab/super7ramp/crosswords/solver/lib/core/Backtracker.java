package com.gitlab.super7ramp.crosswords.solver.lib.core;

import java.util.Optional;

/**
 * Strategy to apply when dead-end is reached.
 */
public interface Backtracker {

    /**
     * Determines the variable to unassign to solve the dead-end reached on given variable.
     *
     * @param variable unassignable variable
     * @return the variable that should be unassigned or {@link Optional#empty()} if none could be found
     */
    Optional<Slot> backtrackFrom(final Slot variable);

}
