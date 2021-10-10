package com.gitlab.super7ramp.crosswords.solver.lib.comparators;

import com.gitlab.super7ramp.crosswords.solver.lib.core.AdaptedDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;

import java.util.Comparator;

/**
 * A collection of {@link Comparator}s.
 */
public final class Comparators {

    /**
     * Private constructor, static methods only.
     */
    private Comparators() {
        // Nothing to do.
    }

    public static Comparator<Slot> byNumberOfCandidates(final AdaptedDictionary dictionary) {
        return Comparator.comparingLong(dictionary::countPossibleValues);
    }

}
