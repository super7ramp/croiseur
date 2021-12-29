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
     * @return <code>true</code> iff the variable has been formally assigned and has not been
     * unassigned yet and has a value (i.e. a connected slot hasn't been unassigned)
     */
    boolean isInstantiated();

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
     * Returns if given string fits inside slot - without consideration on the given string being
     * in a dictionary or not.
     *
     * @param value the value to test
     * @return <code>true</code> if the given value fits inside this slot
     */
    boolean isCompatibleWith(final String value);
}
