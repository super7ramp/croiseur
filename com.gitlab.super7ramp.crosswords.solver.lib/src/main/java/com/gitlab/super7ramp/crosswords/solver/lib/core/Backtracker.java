package com.gitlab.super7ramp.crosswords.solver.lib.core;

import java.util.Set;

/**
 * Strategy to apply when dead-end is reached.
 */
public interface Backtracker {

    /**
     * Determines the variables to unassign to solve the dead-end reached on given variable.
     *
     * @param variable unassignable variable
     * @return the variables that should be unassigned
     */
    Set<Slot> backtrackFrom(final Slot variable);

}
