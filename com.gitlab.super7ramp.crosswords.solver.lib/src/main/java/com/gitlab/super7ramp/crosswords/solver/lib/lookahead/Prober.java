package com.gitlab.super7ramp.crosswords.solver.lib.lookahead;

import com.gitlab.super7ramp.crosswords.solver.lib.core.InternalDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;

import java.math.BigInteger;
import java.util.Collection;

/**
 * Provides some common lookahead functions.
 */
public final class Prober {

    /** The puzzle to probe. */
    private final Probable puzzle;

    /** The dictionary. */
    private final InternalDictionary dictionary;

    /**
     * Constructor.
     *
     * @param aPuzzle     a puzzle to probe
     * @param aDictionary a dictionary
     */
    public Prober(final Probable aPuzzle, final InternalDictionary aDictionary) {
        puzzle = aPuzzle;
        dictionary = aDictionary;
    }

    /**
     * Compute <em>an estimation</em> of the number of solutions for the grid after applying the given assignment by
     * multiplying the estimated number of candidates left for each variable.
     * <p>
     * Note that the estimation may return a value > 0 despite the grid not having an actual solution, as the constraint
     * that candidate cannot be used twice per grid is not taken into account.
     *
     * @param assignment the assignment to evaluate
     * @return the estimated number of solutions for the grid after assignment; As the estimation can be very large,
     * computation is made using {@link BigInteger}
     */
    public BigInteger computeNumberOfSolutionsAfter(final Assignment assignment) {

        final Collection<Slot> probeResult = puzzle.probe(assignment);

        return probeResult.stream()
                .filter(slot -> slot.value().isEmpty())
                .map(dictionary::countPossibleValues)
                .map(BigInteger::valueOf)
                .reduce(BigInteger::multiply)
                .orElse(BigInteger.ONE);

    }
}
