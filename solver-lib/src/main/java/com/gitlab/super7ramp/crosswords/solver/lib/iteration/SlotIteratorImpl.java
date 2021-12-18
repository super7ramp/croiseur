package com.gitlab.super7ramp.crosswords.solver.lib.iteration;

import com.gitlab.super7ramp.crosswords.solver.lib.core.CachedDictionary;
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
     * Comparator of {@link Slot} by number of candidates in {@link CachedDictionary}.
     */
    private static final Comparator<NumberOfCandidatesPerSlot> BY_NUMBER_OF_CANDIDATES =
            Comparator.comparingLong(NumberOfCandidatesPerSlot::numberOfCandidates)
                    .thenComparingInt(pair -> pair.slot.uid().id());
    /**
     * The dictionary.
     */
    private final CachedDictionary dictionary;

    /**
     * All variables.
     */
    private final Collection<Slot> variables;

    /**
     * Constructor.
     *
     * @param slots       the slots
     * @param aDictionary the dictionary
     */
    public SlotIteratorImpl(final Collection<Slot> slots, final CachedDictionary aDictionary) {
        variables = Collections.unmodifiableCollection(slots);
        dictionary = aDictionary;
    }

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
                .map(slot -> new NumberOfCandidatesPerSlot(slot, dictionary.candidatesCount(slot)))
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
        return slot -> slot.value().isEmpty() || !dictionary.candidatesContains(slot, slot.value().get());
    }
}
