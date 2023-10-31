/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.heuristics.instantiation;

import re.belv.croiseur.solver.ginsberg.core.Slot;
import re.belv.croiseur.solver.ginsberg.core.sap.CandidateChooser;
import re.belv.croiseur.solver.ginsberg.dictionary.CachedDictionary;
import re.belv.croiseur.solver.ginsberg.lookahead.Assignment;
import re.belv.croiseur.solver.ginsberg.lookahead.ProbePuzzle;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Implementation of {@link CandidateChooser}.
 * <p>
 * This implementation is guided by the number of solutions left after assignment of the candidate
 * value: The candidate which leaves the bigger number of solutions wins.
 * <p>
 * In other words, the selected value is the value which brings as little constraints on the grid as
 * possible.
 */
final class LeastConstrainingCandidateChooser implements CandidateChooser<Slot, String> {

    /**
     * Associates a candidate to the estimated number of solutions of the grid.
     */
    private record NumberOfSolutionsPerCandidate(String candidate, BigInteger numberOfSolutions) {
        // Nothing to add.
    }

    /** Max number of candidates with solutions to compare. */
    private static final long MAX_NUMBER_OF_CANDIDATES_TO_COMPARE = 10;

    /**
     * Compare candidates by their estimated number of puzzle solutions.<p> The secondary comparator
     * by lexicographic order on candidate is for reproducibility.
     */
    private static final Comparator<NumberOfSolutionsPerCandidate> BY_NUMBER_OF_SOLUTIONS =
            Comparator.comparing(NumberOfSolutionsPerCandidate::numberOfSolutions)
                      .thenComparing(NumberOfSolutionsPerCandidate::candidate);

    /** Filter candidates with at least one puzzle solution. */
    private static final Predicate<NumberOfSolutionsPerCandidate> WITH_SOLUTION =
            probe -> probe.numberOfSolutions.signum() > 0;

    /** The dictionary to pick candidates from. */
    private final CachedDictionary dictionary;

    /** Lookahead util. */
    private final ProbePuzzle probePuzzle;

    /**
     * Constructor.
     *
     * @param probePuzzleArg the puzzle to solve
     * @param dictionaryArg   the dictionary to pick candidates from
     */
    LeastConstrainingCandidateChooser(final ProbePuzzle probePuzzleArg,
                                      final CachedDictionary dictionaryArg) {
        dictionary = dictionaryArg;
        probePuzzle = probePuzzleArg;
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
        final BigInteger numberOfSolutions =
                probePuzzle.computeNumberOfLocalSolutionsAfter(Assignment.of(wordVariable.uid(),
                                                                             candidate));
        return new NumberOfSolutionsPerCandidate(candidate, numberOfSolutions);
    }

}
