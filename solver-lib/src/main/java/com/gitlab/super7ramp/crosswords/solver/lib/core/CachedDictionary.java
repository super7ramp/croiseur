package com.gitlab.super7ramp.crosswords.solver.lib.core;

import java.util.stream.Stream;

/**
 * A dictionary caching results of potentially slow external dictionary.
 * <p>
 * Implementation is synced with puzzle assignments/backtracks in order to minimize the amount of
 * candidates to evaluate.
 */
public interface CachedDictionary {

    /**
     * Returns the candidates for given variable as a new {@link Stream}.
     *
     * @param wordVariable a variable
     * @return the candidates for given variable
     */
    Stream<String> candidates(final Slot wordVariable);

    /**
     * Returns <code>true</code> if and only if the dictionary contains the given candidate for
     * the given slot.
     * <p>
     * To be preferred over filtering stream provided by {@link #candidates(Slot)} if no
     * intermediate stream operation needed, that is to say prefer
     * {@code dictionary.contains(variable, value)} over
     * {@code candidates(slot).anyMatch(value::equals)}.
     *
     * @param value the value to test
     * @return <code>true</code> if and only if the dictionary contains the given candidate for
     * the given slot
     */
    boolean candidatesContains(final Slot wordVariable, final String value);

    /**
     * Returns the number of candidates for given variable.
     * <p>
     * To be preferred over counting stream provided by {@link #candidates(Slot)} if no
     * intermediate stream operation needed, that is to say prefer
     * {@code candidatesCount(variable)} over {@code candidates(variable).count()}.
     *
     * @param wordVariable a variable
     * @return the candidates for given variable
     */
    long candidatesCount(final Slot wordVariable);

    /**
     * Returns the number of candidates for given variable.
     * <p>
     * Similar to {@link #candidatesCount(Slot)} but indicates that cache result shall be refined.
     * To be used when probing candidates, i.e. when puzzle is not in sync with the puzzle data
     * backing this dictionary.
     * <p>
     * Probed slot is a hint for cache optimization (avoid refreshing all values, only connected
     * ones).
     *
     * @param wordVariable   a variable
     * @param probedVariable variable which is not in sync with the puzzle data backing this
     *                       {@link CachedDictionary}; connected slots candidates will be refreshed
     * @return the candidates for given variable
     */
    long refreshedCandidatesCount(final Slot wordVariable, final SlotIdentifier probedVariable);
}
