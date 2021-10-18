package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.lib.comparators.Comparators;
import com.gitlab.super7ramp.crosswords.solver.lib.core.AdaptedDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Puzzle;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.core.SlotIterator;
import com.gitlab.super7ramp.crosswords.solver.lib.grid.SlotIdentifier;

import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Implementation of {@link SlotIterator}.
 */
final class SlotIteratorImpl implements SlotIterator {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(SlotIteratorImpl.class.getName());

    /**
     * All variables.
     */
    private final Collection<Slot> variables;

    private final AdaptedDictionary dictionary;

    /**
     * Comparator of {@link Slot} by number of candidates in {@link AdaptedDictionary}.
     */
    private final Comparator<Slot> comparator;

    /**
     * Constructor.
     *
     * @param puzzle      the grid to solve
     * @param aDictionary the dictionary
     */
    SlotIteratorImpl(
            final Puzzle puzzle, final AdaptedDictionary aDictionary) {
        variables = puzzle.slots();
        comparator = Comparators
                .byNumberOfCandidates(aDictionary)
                // Ordering on UID allows to have reproducible results
                .thenComparing(Slot::uid, Comparator.comparingInt(SlotIdentifier::id));
        dictionary = aDictionary;
    }

    @Override
    public boolean hasNext() {
        LOGGER.finest(() -> variables.stream().map(s -> s.toString() + ": value=" + s.value()).collect(Collectors.joining(System.lineSeparator())));
        return variables.stream().anyMatch(v -> v.value().isEmpty() || !dictionary.contains(v.value().get()));
    }

    @Override
    public Slot next() {
        return variables.stream()
                .filter(v -> v.value().isEmpty() || !dictionary.contains(v.value().get()))
                .max(comparator)
                .orElseThrow(NoSuchElementException::new);
    }

}
