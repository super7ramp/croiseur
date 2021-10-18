package com.gitlab.super7ramp.crosswords.solver.lib.core;

import com.gitlab.super7ramp.crosswords.solver.lib.grid.SlotIdentifier;

import java.util.Set;

/**
 * TODO split blacklist methods in a separate interface extending this one
 */
public interface AdaptedDictionary {

    /**
     * Returns the candidates for given variable.
     *
     * @param wordVariable a variable
     * @return the candidates for given variable
     */
    Set<String> findPossibleValues(final Slot wordVariable);

    /**
     * Returns the number of candidates for given variables.
     *
     * @param wordVariable a variable
     * @return the candidates for given variable
     */
    long countPossibleValues(final Slot wordVariable);

    /**
     * Returns <code>true</code> if and only if the dictionary contains the given value.
     *
     * @param value the value to test
     * @return <code>true</code> if and only if the dictionary contains the given value
     */
    boolean contains(final String value);

    /**
     * Prevent this value from being used.
     *
     * @param value value to lock
     */
    void lock(final String value);

    /**
     * Allows a previously locked value to be used again.
     *
     * @param value value to unlock
     */
    void unlock(final String value);

    /**
     * Blacklist the given value for given variable.
     *
     * @param wordVariable the variable for which the value should be blacklisted
     * @param value        the value to blacklist
     */
    void blacklist(final SlotIdentifier wordVariable, final String value);

    /**
     * Rehabilitate all blacklisted elements.
     */
    void resetBlacklist();
}
