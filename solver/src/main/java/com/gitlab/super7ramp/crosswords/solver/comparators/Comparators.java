package main.java.com.gitlab.super7ramp.crosswords.solver.comparators;

import main.java.com.gitlab.super7ramp.crosswords.db.WordDatabase;
import main.java.com.gitlab.super7ramp.crosswords.solver.WordVariable;

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

    public static Comparator<WordVariable> byNumberOfCandidates(final WordDatabase dictionary) {
        return Comparator.comparingLong(dictionary::countPossibleValues);
    }

}
