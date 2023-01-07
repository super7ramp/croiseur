package com.gitlab.super7ramp.crosswords.solver.ginsberg.core;

import java.util.Collection;
import java.util.Optional;

/**
 * Read/write access to a slot of a crossword puzzle.
 */
// TODO to split, at least assign/unassign from the rest
public interface Slot {

    /**
     * Unique identifier.
     */
    SlotIdentifier uid();

    /**
     * Return the value of the variable, if instantiated.
     *
     * @return the value of the variable, if instantiated
     */
    Optional<String> value();

    /**
     * Return whether the variable is instantiated.
     *
     * @return {@code true} iff the variable is instantiated.
     */
    boolean isInstantiated();

    /**
     * Return whether the variable is connected to the given other variable.
     *
     * @param other the other variable
     * @return {@code true} iff the two variables are connected
     */
    default boolean isConnectedTo(final Slot other) {
        return isConnectedTo(other.uid());
    }

    /**
     * Return whether the variable is connected to the given other variable.
     *
     * @param other the other variable
     * @return {@code true} iff the two variables are connected
     */
    boolean isConnectedTo(final SlotIdentifier other);

    /**
     * Returns the identifiers of the slots connected to this slot.
     *
     * @return the identifiers of the slots connected to this slot
     */
    Collection<SlotIdentifier> connectedSlots();

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
     * @return the unassigned value
     * @throws IllegalStateException if the variable is not instantiated
     */
    String unassign();

    /**
     * Returns if given string fits inside slot - without consideration on the given string being
     * in a dictionary or not.
     *
     * @param value the value to test
     * @return {@code true} if the given value fits inside this slot
     */
    boolean isCompatibleWith(final String value);
}
