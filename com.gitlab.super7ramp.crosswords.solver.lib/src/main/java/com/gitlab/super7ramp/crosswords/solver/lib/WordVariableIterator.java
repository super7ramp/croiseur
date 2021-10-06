package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.db.WordDatabase;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiPredicate;

import static com.gitlab.super7ramp.crosswords.util.function.PartialApplications.partialApply;

/**
 * Encapsulates the heuristics for choosing the next variable to instantiate.
 */
public final class WordVariableIterator implements Iterator<WordVariable> {

    /** Check whether a variable has been instantiated. */
    private static final BiPredicate<Map<WordVariable, String>, WordVariable> NOT_YET_INSTANTIATED =
            (m, v) -> m.get(v) == null;

    /** Instantiated variables. */
    private final Map<WordVariable, String> partialAssignments;

    /** All variables. */
    private final Collection<WordVariable> variables;

    /** Comparator of {@link WordVariable} by number of candidates in {@link WordDatabase}. */
    private final Comparator<WordVariable> wordVariableComparator;

    /**
     * Constructor.
     *
     * @param sap the grid to solve
     * @param somePartialAssignments some words known to be already instantiated
     * @param aWordVariableComparator variable comparator
     */
    WordVariableIterator(
            final CrosswordProblem sap,
            final Map<WordVariable, String> somePartialAssignments,
            final Comparator<WordVariable> aWordVariableComparator) {
        partialAssignments = Collections.unmodifiableMap(somePartialAssignments);
        variables = sap.variables();
        wordVariableComparator = aWordVariableComparator;
    }

    @Override
    public boolean hasNext() {
        return partialAssignments.keySet().size() < variables.size();
    }

    @Override
    public WordVariable next() {
        return variables.stream()
                .filter(partialApply(NOT_YET_INSTANTIATED, partialAssignments))
                .max(wordVariableComparator)
                .orElseThrow(NoSuchElementException::new);
    }


}
