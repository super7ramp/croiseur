package com.gitlab.super7ramp.crosswords.solver.lib.core;

import com.gitlab.super7ramp.crosswords.solver.lib.util.solver.Variable;

/**
 * Read/write access to a slot of a crossword puzzle.
 */
public interface Slot extends Variable<String> {

    /**
     * Unique identifier.
     */
    SlotIdentifier uid();

    /**
     * Returns if given string fits inside slot - without consideration on the given string being in a dictionary or
     * not.
     *
     * @param value the value to test
     * @return <code>true</code> if the given value fits inside this slot
     */
    boolean isCompatibleWith(final String value);
}
