package com.gitlab.super7ramp.crosswords.solver.lib.instantiation;

import com.gitlab.super7ramp.crosswords.solver.lib.core.CandidateChooser;
import com.gitlab.super7ramp.crosswords.solver.lib.core.InternalDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.lookahead.Assignment;
import com.gitlab.super7ramp.crosswords.solver.lib.lookahead.Probable;
import com.gitlab.super7ramp.crosswords.solver.lib.lookahead.Prober;
import com.gitlab.super7ramp.crosswords.solver.lib.util.Pair;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

/**
 * Implementation of {@link CandidateChooser}.
 * <p>
 * This implementation is guided by the number of solutions left after assignment of the candidate value:
 * The candidate which leaves the bigger number of solutions wins.
 * <p>
 * In other words, the selected value is the value which brings as little constraints on the grid as possible.
 */
public final class CandidateChooserImpl implements CandidateChooser {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(CandidateChooserImpl.class.getName());

    /** Filter candidates with at least one puzzle solution. */
    private static final Predicate<Pair<String, BigInteger>> WITH_SOLUTION = probe ->
            probe.right().compareTo(BigInteger.ZERO) > 0;

    /**
     * Compare candidates by their estimated number of puzzle solutions.<p>
     * The secondary comparator by lexicographic order is for reproducibility.
     */
    private static final Comparator<Pair<String, BigInteger>> BY_NUMBER_OF_SOLUTIONS =
            Comparator.comparing(Pair<String, BigInteger>::right).thenComparing(Pair::left);

    /** The dictionary to pick candidates from. */
    private final InternalDictionary dictionary;

    /** Lookahead util. */
    private final Prober prober;

    /**
     * Constructor.
     *
     * @param aPuzzle     the puzzle to solve
     * @param aDictionary the dictionary to pick candidates from
     */
    public CandidateChooserImpl(final Probable aPuzzle, final InternalDictionary aDictionary) {
        dictionary = aDictionary;
        prober = new Prober(aPuzzle, aDictionary);
    }

    @Override
    public Optional<String> find(final Slot wordVariable) {
        return dictionary
                .findPossibleValues(wordVariable)
                .map(candidate -> probe(wordVariable, candidate))
                .filter(WITH_SOLUTION)
                .max(BY_NUMBER_OF_SOLUTIONS)
                .map(Pair::left);
    }

    /**
     * Builds a new {@link Pair} of a candidate with the looked-ahead number of solutions for the grid.
     *
     * @param wordVariable the variable
     * @param candidate    the candidate
     * @return a new {@link Pair} of a candidate with the looked-ahead number of solutions for the grid
     */
    private Pair<String, BigInteger> probe(final Slot wordVariable, final String candidate) {
        return new Pair<>(candidate, prober.computeNumberOfSolutionsAfter(Assignment.of(wordVariable.uid(), candidate)));
    }

}
