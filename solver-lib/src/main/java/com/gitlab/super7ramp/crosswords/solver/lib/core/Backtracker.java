package com.gitlab.super7ramp.crosswords.solver.lib.core;

/**
 * Strategy to apply when dead-end is reached.
 */
public interface Backtracker {

    /**
     * Determines the variable to unassign to solve the dead-end reached on given variable.
     *
     * @param variable unassignable variable
     * @return the variable that should be unassigned
     */
    Slot backtrackFrom(final Slot variable);

}
