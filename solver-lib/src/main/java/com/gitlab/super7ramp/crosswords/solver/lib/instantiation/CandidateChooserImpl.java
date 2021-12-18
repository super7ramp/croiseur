package com.gitlab.super7ramp.crosswords.solver.lib.instantiation;

import com.gitlab.super7ramp.crosswords.solver.lib.core.CachedDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.CandidateChooser;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.lookahead.Assignment;
import com.gitlab.super7ramp.crosswords.solver.lib.lookahead.Probable;
import com.gitlab.super7ramp.crosswords.solver.lib.lookahead.Prober;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Implementation of {@link CandidateChooser}.
 * <p>
 * This implementation is guided by the number of solutions left after assignment of the
 * candidate value: The candidate which leaves the bigger number of solutions wins.
 * <p>
 * In other words, the selected value is the value which brings as little constraints on the grid
 * as possible.
 */
public final class CandidateChooserImpl implements CandidateChooser {

    /**
     * Associates a candidate to the estimated number of solutions of the grid.
     */
    private record NumberOfSolutionsPerCandidate(String candidate, BigInteger numberOfSolutions) {
        // Nothing to add.
    }

    /** Max number of candidates with solutions to compare. */
    private static final long MAX_NUMBER_OF_CANDIDATES_TO_COMPARE = 10;

    /**
     * Compare candidates by their estimated number of puzzle solutions.<p>
     * The secondary comparator by lexicographic order on candidate is for reproducibility.
     */
    private static final Comparator<NumberOfSolutionsPerCandidate> BY_NUMBER_OF_SOLUTIONS =
            Comparator.comparing(NumberOfSolutionsPerCandidate::numberOfSolutions)
                      .thenComparing(NumberOfSolutionsPerCandidate::candidate);

    /** Filter candidates with at least one puzzle solution. */
    private static final Predicate<NumberOfSolutionsPerCandidate> WITH_SOLUTION =
            probe -> probe.numberOfSolutions.compareTo(BigInteger.ZERO) > 0;

    /** The dictionary to pick candidates from. */
    private final CachedDictionary dictionary;

    /** Lookahead util. */
    private final Prober prober;

    /**
     * Constructor.
     *
     * @param aPuzzle     the puzzle to solve
     * @param aDictionary the dictionary to pick candidates from
     */
    public CandidateChooserImpl(final Probable aPuzzle, final CachedDictionary aDictionary) {
        dictionary = aDictionary;
        prober = new Prober(aPuzzle, aDictionary);
    }

    @Override
    public Optional<String> find(final Slot wordVariable) {

        return dictionary.candidates(wordVariable)
                         .map(candidate -> probe(wordVariable, candidate))
                         .filter(WITH_SOLUTION)
                         .limit(MAX_NUMBER_OF_CANDIDATES_TO_COMPARE)
                         .max(BY_NUMBER_OF_SOLUTIONS)
                         .map(NumberOfSolutionsPerCandidate::candidate);

    }

    /**
     * Builds a new {@link NumberOfSolutionsPerCandidate} of a candidate with the looked-ahead
     * number of solutions for the grid.
     *
     * @param wordVariable the variable
     * @param candidate    the candidate
     * @return a new {@link NumberOfSolutionsPerCandidate} of a candidate with the looked-ahead
     * number of solutions for the grid
     */
    private NumberOfSolutionsPerCandidate probe(final Slot wordVariable, final String candidate) {
        return new NumberOfSolutionsPerCandidate(candidate,
                prober.computeNumberOfSolutionsAfter(Assignment.of(wordVariable.uid(), candidate)));
    }

}
