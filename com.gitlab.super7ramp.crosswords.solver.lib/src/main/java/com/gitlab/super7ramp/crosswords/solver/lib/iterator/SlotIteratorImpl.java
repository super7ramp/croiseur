package com.gitlab.super7ramp.crosswords.solver.lib.iterator;

import com.gitlab.super7ramp.crosswords.solver.lib.core.InternalDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.core.SlotIterator;
import com.gitlab.super7ramp.crosswords.solver.lib.util.Pair;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Implementation of {@link SlotIterator}.
 */
public final class SlotIteratorImpl implements SlotIterator {

    /** All variables. */
    private final Collection<Slot> variables;

    /** Comparator of {@link Slot} by number of candidates in {@link InternalDictionary}. */
    private static final Comparator<Pair<Slot, Long>> BY_NUMBER_OF_CANDIDATES =
            Comparator.comparingLong(Pair<Slot, Long>::right).thenComparingInt(pair -> pair.left().uid().id());
    /** Disassociate the slot from a (slot, number of candidates) {@link Pair}. */
    private static final Function<Pair<Slot, Long>, Slot> SLOT_FROM_PAIR = Pair::left;
    /** The dictionary. */
    private final InternalDictionary dictionary;

    /**
     * Constructor.
     *
     * @param slots       the slots
     * @param aDictionary the dictionary
     */
    public SlotIteratorImpl(
            final Collection<Slot> slots, final InternalDictionary aDictionary) {
        variables = Collections.unmodifiableCollection(slots);
        dictionary = aDictionary;
    }

    @Override
    public boolean hasNext() {
        return variables.stream().anyMatch(unassignedSlot());
    }

    @Override
    public Slot next() {
        return variables.stream()
                .filter(unassignedSlot())
                .map(pairOfSlotAndNumberOfCandidates())
                .min(BY_NUMBER_OF_CANDIDATES)
                .map(SLOT_FROM_PAIR)
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

    /**
     * Count the number of possible values for this slot and associate it to the slot as a {@link Pair}.
     *
     * @return the association of the slot and its number of candidates
     */
    private Function<Slot, Pair<Slot, Long>> pairOfSlotAndNumberOfCandidates() {
        return slot -> new Pair<>(slot, dictionary.countPossibleValues(slot));
    }

}
