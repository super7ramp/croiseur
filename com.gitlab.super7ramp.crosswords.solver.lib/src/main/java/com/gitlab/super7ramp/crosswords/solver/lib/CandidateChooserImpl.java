package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.lib.core.AdaptedDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Assignment;
import com.gitlab.super7ramp.crosswords.solver.lib.core.CandidateChooser;
import com.gitlab.super7ramp.crosswords.solver.lib.core.ProbablePuzzle;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.util.function.Accumulators;

import java.util.Collection;
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
final class CandidateChooserImpl implements CandidateChooser {

    /** A probed value associated with the estimated number of solutions for the puzzle. */
    private static record ProbedValue(String value, long estimatedNumberOfSolutions) {
        // Nothing more.
    }

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(CandidateChooserImpl.class.getName());

    /** Filter {@link ProbedValue}s with a puzzle solution. */
    private static final Predicate<ProbedValue> WITH_SOLUTION = probe -> probe.estimatedNumberOfSolutions != 0;

    /**
     * Compare {@link ProbedValue}s by their estimated number of puzzle solutions.<p>
     * The secondary comparator by lexicographic order is for reproducibility.
     */
    private static final Comparator<ProbedValue> BY_NUMBER_OF_SOLUTIONS =
            Comparator.comparingLong(ProbedValue::estimatedNumberOfSolutions).thenComparing(ProbedValue::value);

    /**
     * The dictionary to pick candidates from.
     */
    private final AdaptedDictionary dictionary;

    /**
     * The puzzle.
     */
    private final ProbablePuzzle puzzle;

    /**
     * Constructor.
     *
     * @param aPuzzle     the puzzle to solve
     * @param aDictionary the dictionary to pick candidates from
     */
    CandidateChooserImpl(final ProbablePuzzle aPuzzle, final AdaptedDictionary aDictionary) {
        puzzle = aPuzzle;
        dictionary = aDictionary;
    }

    @Override
    public Optional<String> find(final Slot wordVariable) {
        return dictionary
                .findPossibleValues(wordVariable)
                .stream()
                .map(candidate -> probe(wordVariable, candidate))
                .max(BY_NUMBER_OF_SOLUTIONS)
                .filter(WITH_SOLUTION)
                .map(ProbedValue::value);
    }

    /**
     * Builds a new {@link ProbedValue}.
     *
     * @param wordVariable the variable
     * @param candidate    the candidate
     * @return the {@link ProbedValue}
     */
    private ProbedValue probe(final Slot wordVariable, final String candidate) {
        return new ProbedValue(candidate, computeNumberOfSolutions(wordVariable, candidate));
    }

    /**
     * Compute <em>an estimation</em> of the number of solutions of the newly computed grid by multiplying the number of
     * candidates left for each variable.
     * <p>
     * Note that estimation may return a value > 0 despite the grid not having an actual solution - e.g. if n
     * variables share n-1 candidates.
     *
     * @param candidate the candidate to probe
     * @return the "number of solutions" for the grid if this candidate is chosen
     */
    // TODO optimization: Filter on connected slots instead of the whole grid
    // TODO optimization: Short-circuit reduce when a count is 0 (maybe use Stream#takeWhile())
    // TODO optimization: Cache?
    private long computeNumberOfSolutions(final Slot variable, final String candidate) {
        final Collection<Slot> probeResult = puzzle.probe(Assignment.of(variable.uid(), candidate)).slots();

        final long numberOfSolutions = probeResult.stream()
                .filter(slot -> slot.value().isEmpty())
                .map(dictionary::countPossibleValues)
                .reduce(Accumulators.multiplyLong())
                .orElse(1L);
        LOGGER.fine(() -> "Cumulated number of solutions left after assigning " + variable + " with " + candidate + ": " + numberOfSolutions);

        return numberOfSolutions;
    }
}
