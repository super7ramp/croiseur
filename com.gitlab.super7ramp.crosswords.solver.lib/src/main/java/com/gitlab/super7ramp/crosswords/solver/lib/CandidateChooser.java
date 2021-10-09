package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.lib.db.WordDatabase;
import com.gitlab.super7ramp.crosswords.solver.lib.util.function.Accumulators;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

/**
 * Encapsulates the heuristics for selecting the value of a variable.
 */
final class CandidateChooser {

    /** The dictionary to pick candidates from. */
    private final WordDatabase dictionary;

    /** The problem. */
    private final CrosswordProblem problem;

    /**
     * Constructor.
     *
     * @param aDictionary the dictionary to pick candidates from
     */
    CandidateChooser(final CrosswordProblem aProblem, final WordDatabase aDictionary) {
        problem = aProblem;
        dictionary = aDictionary;
    }

    /**
     * Choose a candidate for the given variable.
     *
     * @param wordVariable word variable to choose a candidate for
     * @return the appropriate candidate, or {@link Optional#empty()} if no suitable candidate can be found
     */
    Optional<String> find(final WordVariable wordVariable) {
        /*
         * Select the candidate which brings as little constraints on the grid as possible.
         */
        return dictionary
                .findPossibleValues(wordVariable)
                .stream()
                .max(byNumberOfSolutionsLeft(wordVariable));
    }

    /**
     * Creates a {@link Comparator} of candidate based on the number of solutions they left to for the rest of the
     * grid.
     *
     * @param wordVariable the variable for which this candidate is tried
     * @return as described above
     */
    private Comparator<String> byNumberOfSolutionsLeft(final WordVariable wordVariable) {
        return Comparator.comparingLong((candidate) -> computeNumberOfSolutions(wordVariable, candidate));
    }

    /**
     * @return a new set of variables
     */
    private Collection<WordVariable> probe(final WordVariable variable, final String probedCandidate) {
        return problem.probeAssignment(variable.uid(), probedCandidate).variables();
    }

    /**
     * Compute "the number of solutions" of the newly computed grid by multiplying the number of candidates left
     * for each variable.
     *
     * @param candidate the candidate to probe
     * @return the "number of solutions" for the grid if this candidate is chosen
     */
    private long computeNumberOfSolutions(final WordVariable variable, final String candidate) {
        final Collection<WordVariable> probeResult = probe(variable, candidate);
        return probeResult.stream()
                .map(dictionary::countPossibleValues)
                .reduce(Accumulators.multiplyLong())
                .orElse(0L);
    }
}
