package com.gitlab.super7ramp.crosswords.solver.api;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Dictionary interface (to be provided).
 */
public interface Dictionary {

    /**
     * Search for words matching the given {@link Predicate}.
     *
     * @param predicate the predicate to satisfy
     * @return a set of words matching the given pattern
     */
    Set<String> lookup(final Predicate<String> predicate);

    /**
     * Count matches for given {@link Predicate}.
     *
     * @param predicate the predicate for which to count matches
     * @return the number of matches for given predicate
     */
    long countMatches(final Predicate<String> predicate);

    /**
     * Returns <code>true</code> if and only if the dictionary contains the given value.
     *
     * @param value the value to test
     * @return <code>true</code> if and only if the dictionary contains the given value
     */
    boolean contains(final String value);
}
