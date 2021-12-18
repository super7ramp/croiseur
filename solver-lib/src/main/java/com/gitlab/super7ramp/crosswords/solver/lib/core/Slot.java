package com.gitlab.super7ramp.crosswords.solver.lib.core;

import java.util.Optional;

/**
 * Read/write access to a slot of a crossword puzzle.
 */
public interface Slot {

    /**
     * Unique identifier.
     */
    SlotIdentifier uid();

    /**
     * @return the value of the variable, if assigned
     */
    Optional<String> value();

    /**
     * Assign a value to the variable.
     *
     * @param value the value to assign
     */
    void assign(final String value);

    /**
     * Clear any assignment on this variable.
     *
     * @return the unassigned value, if any
     */
    Optional<String> unassign();

    /**
     * Returns if given string fits inside slot - without consideration on the given string being in a dictionary or
     * not.
     *
     * @param value the value to test
     * @return <code>true</code> if the given value fits inside this slot
     */
    boolean isCompatibleWith(final String value);

    /**
     * Whether this slot has been pre-filled at grid construction. It is immutable - solver shall not try to assign
     * anything, otherwise a runtime exception will be run.
     *
     * @return whether this slot has been pre-filled
     */
    boolean isPrefilled();
}
