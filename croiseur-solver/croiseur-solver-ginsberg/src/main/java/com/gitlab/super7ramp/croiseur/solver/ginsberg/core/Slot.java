/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.core;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Read/write access to a slot of a crossword puzzle.
 */
// TODO to split, at least assign/unassign from the rest
public interface Slot {

    /**
     * Returns the slot's unique identifier.
     *
     * @return the slot's unique identifier
     */
    SlotIdentifier uid();

    /**
     * Return the value of the variable, if instantiated.
     *
     * @return the value of the variable, if instantiated
     */
    Optional<String> value();

    /**
     * Returns this slot as a pattern.
     * <p>
     * Non-filled boxes will be replaced with the space character (' ').
     * <p>
     * If the slot is assigned, then pattern is strictly equal to the {@link #value()}.
     *
     * @return the slot as a pattern
     */
    String asPattern();

    /**
     * Return whether the variable is instantiated.
     *
     * @return {@code true} iff the variable is instantiated.
     */
    boolean isInstantiated();

    /**
     * Returns the slots connected to this slot.
     *
     * @return the slots connected to this slot
     */
    Stream<? extends Slot> connectedSlots();

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
