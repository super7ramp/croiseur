package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.lib.comparators.Comparators;
import com.gitlab.super7ramp.crosswords.solver.lib.core.AdaptedDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Puzzle;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.core.SlotIterator;

import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * Implementation of {@link SlotIterator}.
 */
final class SlotIteratorImpl implements SlotIterator {

    /**
     * All variables.
     */
    private final Collection<Slot> variables;

    /**
     * Comparator of {@link Slot} by number of candidates in {@link AdaptedDictionary}.
     */
    private final Comparator<Slot> comparator;

    /**
     * Constructor.
     *
     * @param puzzle     the grid to solve
     * @param dictionary the dictionary
     */
    SlotIteratorImpl(
            final Puzzle puzzle, final AdaptedDictionary dictionary) {
        variables = puzzle.slots();
        comparator = Comparators.byNumberOfCandidates(dictionary);
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
