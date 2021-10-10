package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.lib.db.WordDatabase;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Encapsulates the heuristics for choosing the next variable to instantiate.
 */
final class SlotIterator implements Iterator<Slot> {

    /**
     * All variables.
     */
    private final Collection<Slot> variables;

    /**
     * Comparator of {@link Slot} by number of candidates in {@link WordDatabase}.
     */
    private final Comparator<Slot> comparator;

    /**
     * Constructor.
     *
     * @param puzzle      the grid to solve
     * @param aComparator variable comparator
     */
    SlotIterator(
            final Puzzle puzzle,
            final Comparator<Slot> aComparator) {
        variables = puzzle.slots();
        comparator = aComparator;
    }

    @Override
    public boolean hasNext() {
        return variables.stream().anyMatch(v -> v.value().isEmpty());
    }

    @Override
    public Slot next() {
        return variables.stream()
                .filter(v -> v.value().isEmpty())
                .max(comparator)
                .orElseThrow(NoSuchElementException::new);
    }

}
