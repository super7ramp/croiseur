package com.gitlab.super7ramp.crosswords.solver.lib.db;

import com.gitlab.super7ramp.crosswords.solver.lib.WordVariable;

import java.util.Set;

/**
 * TODO split blacklist methods in a separate interface extending this one
 */
public interface WordDatabase {

    /**
     * Returns the candidates for given variable.
     *
     * @param wordVariable a variable
     * @return the candidates for given variable
     */
    Set<String> findPossibleValues(final WordVariable wordVariable);

    /**
     * Returns the number of candidates for given variables.
     *
     * @param wordVariable a variable
     * @return the candidates for given variable
     */
    long countPossibleValues(final WordVariable wordVariable);

    /**
     * Blacklist the given value for given variable.
     *
     * @param wordVariable the variable for which the value should be blacklisted
     * @param value        the value to blacklist
     */
    void blacklist(final WordVariable wordVariable, final String value);

    /**
     * Rehabilitate all blacklisted elements.
     */
    void resetBlacklist();
}
