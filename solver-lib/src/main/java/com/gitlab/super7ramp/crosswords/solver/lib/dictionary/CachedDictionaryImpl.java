package com.gitlab.super7ramp.crosswords.solver.lib.dictionary;

import com.gitlab.super7ramp.crosswords.solver.api.Dictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.core.SlotIdentifier;
import com.gitlab.super7ramp.crosswords.solver.lib.elimination.EliminationSpace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

/**
 * Implementation of {@link CachedDictionary}.
 */
final class CachedDictionaryImpl implements CachedDictionaryWriter {

    /** Cached dictionary. */
    private final DictionaryCache<Slot, String> cache;

    /** Eligibility. */
    private final BiPredicate<Slot, String> eligibility;

    /**
     * Constructor.
     *
     * @param dictionary a dictionary
     * @param slots      the slots
     */
    CachedDictionaryImpl(final Dictionary dictionary, final Collection<Slot> slots,
                         final EliminationSpace eliminationSpace) {
        eligibility = (slot, word) -> slot.isCompatibleWith(word) &&
                !eliminationSpace.eliminations(slot.uid()).contains(word);
        cache = populateCache(dictionary, slots, eligibility);
    }

    /**
     * Create the dictionary cache.
     *
     * @param dictionary  the external dictionary
     * @param slots       the puzzle's slots
     * @param eligibility the word eligibility predicate per slot
     * @return the created {@link DictionaryCache}
     */
    private static DictionaryCache<Slot, String> populateCache(final Dictionary dictionary,
                                                               final Collection<Slot> slots,
                                                               final BiPredicate<Slot, String> eligibility) {
        final Map<Slot, Collection<String>> initialLookup = new HashMap<>();
        for (final Slot slot : slots) {
            /*
             * Sort result of lookup to have reproducible results
             *
             * Avoid slow TreeSet, a simple list is sufficient for holding the sorted initial
             * lookup result since it is not modified.
             */
            final List<String> lookupResult =
                    new ArrayList<>(dictionary.lookup(slot::isCompatibleWith));
            lookupResult.sort(Comparator.naturalOrder());
            initialLookup.put(slot, lookupResult);
        }
        return new DictionaryCache<>(initialLookup, eligibility);
    }

    @Override
    public Stream<String> candidates(final Slot wordVariable) {
        return cache.candidates(wordVariable).stream();
    }

    @Override
    public boolean candidatesContains(final Slot wordVariable, final String value) {
        return cache.contains(wordVariable, value);
    }

    @Override
    public long candidatesCount(final Slot wordVariable) {
        return cache.candidates(wordVariable).size();
    }

    @Override
    public long refreshedCandidatesCount(final Slot wordVariable,
                                         final SlotIdentifier probedVariable) {
        final Collection<String> candidates = cache.candidates(wordVariable);
        final long count;
        if (wordVariable.isConnectedTo(probedVariable)) {
            count = candidates.stream()
                              .filter(word -> eligibility.test(wordVariable, word))
                              .count();
        } else {
            count = candidates.size();
        }
        return count;
    }

    @Override
    public void invalidateCache(final Slot unassignedSlot) {
        cache.invalidateCache(slot -> slot.isConnectedTo(unassignedSlot) || slot.equals(unassignedSlot));
    }

    @Override
    public void updateCache(final Slot assignedSlot) {
        cache.updateCache(slot -> slot.isConnectedTo(assignedSlot));
    }

}
