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
     * @return the value of the variable, if any
     */
    Optional<String> value();

    /**
     * Return whether the variable has a value.
     * <p>
     * It is equivalent to {@code value().isPresent()}, just a bit cheaper.
     * <p>
     * Note that a slot may have a value without having been ever assigned (e.g. all boxes
     * have been filled by assigning connected slots).
     *
     * @return <code>true</code> iff the slot has a value
     */
    boolean hasValue();

    /**
     * The ratio of empty boxes inside this slot, as a percentage.
     *
     * @return the ratio as an integer in [0,100]; 100 indicates the variable has no box filled;
     * 0 indicates the variable is fully filled (i.e. has a value)
     */
    int emptyBoxRatio();

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
