package com.gitlab.super7ramp.crosswords.solver.lib.iterator;

import com.gitlab.super7ramp.crosswords.solver.lib.core.InternalDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.core.SlotIterator;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * Implementation of {@link SlotIterator}.
 */
public final class SlotIteratorImpl implements SlotIterator {

    /**
     * Constructor.
     *
     * @param slots       the slots
     * @param aDictionary the dictionary
     */
    public SlotIteratorImpl(final Collection<Slot> slots, final InternalDictionary aDictionary) {
        variables = Collections.unmodifiableCollection(slots);
        dictionary = aDictionary;
    }

    /** Comparator of {@link Slot} by number of candidates in {@link InternalDictionary}. */
    private static final Comparator<NumberOfCandidatesPerSlot> BY_NUMBER_OF_CANDIDATES =
            Comparator.comparingLong(NumberOfCandidatesPerSlot::numberOfCandidates)
                    .thenComparingInt(pair -> pair.slot.uid().id());

    /** All variables. */
    private final Collection<Slot> variables;

    /** The dictionary. */
    private final InternalDictionary dictionary;

    /**
     * Associates a {@link Slot} to its number of candidates.
     */
    private record NumberOfCandidatesPerSlot(Slot slot, long numberOfCandidates) {
        // Nothing to add.
    }

    @Override
    public boolean hasNext() {
        return variables.stream().anyMatch(unassignedSlot());
    }

    @Override
    public Slot next() {
        return variables.stream()
                .filter(unassignedSlot())
                .map(slot -> new NumberOfCandidatesPerSlot(slot, dictionary.countPossibleValues(slot)))
                .min(BY_NUMBER_OF_CANDIDATES)
                .map(NumberOfCandidatesPerSlot::slot)
                .orElseThrow(NoSuchElementException::new);
    }

    /**
     * Filter that includes slot without values and slots whose values are not in dictionary.
     *
     * @return the filter
     */
    private Predicate<Slot> unassignedSlot() {
        return slot -> slot.value().isEmpty() || !dictionary.contains(slot.value().get());
    }
}
