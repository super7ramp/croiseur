package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.db.WordDatabase;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Encapsulates the heuristics for choosing the next variable to instantiate.
 */
public final class WordVariableIterator implements Iterator<WordVariable> {

    /** All variables. */
    private final Collection<WordVariable> variables;

    /** Comparator of {@link WordVariable} by number of candidates in {@link WordDatabase}. */
    private final Comparator<WordVariable> wordVariableComparator;

    /**
     * Constructor.
     *
     * @param sap the grid to solve
     * @param aWordVariableComparator variable comparator
     */
    WordVariableIterator(
            final CrosswordProblem sap,
            final Comparator<WordVariable> aWordVariableComparator) {
        variables = sap.variables();
        wordVariableComparator = aWordVariableComparator;
    }

    @Override
    public boolean hasNext() {
        return variables.stream().anyMatch(v -> v.value().isEmpty());
    }

    @Override
    public WordVariable next() {
        return variables.stream()
                .filter(v -> v.value().isEmpty())
                .max(wordVariableComparator)
                .orElseThrow(NoSuchElementException::new);
    }

}
