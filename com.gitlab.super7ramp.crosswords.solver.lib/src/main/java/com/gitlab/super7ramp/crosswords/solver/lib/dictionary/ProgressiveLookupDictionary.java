package com.gitlab.super7ramp.crosswords.solver.lib.dictionary;

import com.gitlab.super7ramp.crosswords.solver.lib.core.InternalDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;

import java.util.stream.Stream;

/**
 * A decorator of {@link InternalDictionary} that simply limits the stream of returned values.
 */
public final class ProgressiveLookupDictionary implements InternalDictionary {

    /** Actual dictionary. */
    private final InternalDictionary actual;

    /** Maximum number of results to return. */
    private final long maximumNumberOfResults;

    /**
     * Constructor.
     *
     * @param anActual                actual dictionary
     * @param aMaximumNumberOfResults limit to the {@link Stream} returned by {@link #findPossibleValues(Slot)}
     */
    public ProgressiveLookupDictionary(final InternalDictionary anActual, final long aMaximumNumberOfResults) {
        actual = anActual;
        maximumNumberOfResults = aMaximumNumberOfResults;
    }

    /**
     * Constructor.
     *
     * @param anActual actual dictionary
     */
    public ProgressiveLookupDictionary(final InternalDictionary anActual) {
        this(anActual, Long.MAX_VALUE);
    }

    @Override
    public Stream<String> findPossibleValues(final Slot wordVariable) {
        return actual.findPossibleValues(wordVariable).limit(maximumNumberOfResults);
    }

    @Override
    public long countPossibleValues(final Slot wordVariable) {
        return findPossibleValues(wordVariable).count();
    }

    @Override
    public boolean contains(final String value) {
        return actual.contains(value);
    }

    @Override
    public void use(final String value) {
        actual.use(value);
    }

    @Override
    public void free(final String value) {
        actual.free(value);
    }

    @Override
    public void blacklist(final Slot wordVariable, final String value) {
        actual.blacklist(wordVariable, value);
    }
}
