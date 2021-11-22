package com.gitlab.super7ramp.crosswords.solver.lib.core;

import java.util.stream.Stream;

/**
 *
 */
// TODO Split use/free methods.
public interface InternalDictionary {

    /**
     * Returns the candidates for given variable.
     *
     * @param wordVariable a variable
     * @return the candidates for given variable
     */
    Stream<String> findPossibleValues(final Slot wordVariable);

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
     * Mark this value as used.
     * <p>
     * This value will not show up in search results anymore, unless {@link #free(String)} is called.
     *
     * @param value value to mark as used
     * @see #free(String)
     */
    void use(final String value);

    /**
     * Mark this value as free to be used again.
     *
     * @param value value to mark as free
     * @see #use(String)
     */
    void free(final String value);

}
