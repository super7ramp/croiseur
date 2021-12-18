package com.gitlab.super7ramp.crosswords.solver.lib.dictionary;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static java.util.function.Predicate.not;

/**
 * Dictionary cache.
 */
final class DictionaryCache<SlotT, ValueT> {

    /**
     * Contains the initial candidates for each slot, after lookup in the real dictionary.
     * <p>
     * It serves as a first cache to avoid filtering all the dictionary again. It is assumed that
     * subsequent requests
     * will be more restrictive than the first lookup.
     */
    private final Map<SlotT, Collection<ValueT>> initial;

    /**
     * The current candidates for each slot.
     */
    private final Map<SlotT, Collection<ValueT>> current;

    /**
     * The eligibility predicate for a word to be a valid candidate for a slot.
     */
    private final BiPredicate<SlotT, ValueT> eligibility;

    /**
     * Constructor.
     *
     * @param someInitialCandidates initial lookup result
     * @param anEligibility         slot-word eligibility predicate for future updates
     */
    DictionaryCache(final Map<SlotT, Collection<ValueT>> someInitialCandidates,
                    final BiPredicate<SlotT, ValueT> anEligibility) {
        eligibility = anEligibility;
        initial = someInitialCandidates;
        current = new HashMap<>(someInitialCandidates.size());
        // Using LinkedHashSet to preserve order of initial lookup and have reproducible results
        initial.forEach((slot, value) -> current.put(slot, new LinkedHashSet<>(value)));
    }

    /**
     * Returns <code>true</code> if and only if the cache contains the given value.
     *
     * @param value the value to test
     * @return <code>true</code> if and only if the cache contains the given value
     */
    boolean contains(final SlotT slot, final ValueT value) {
        return current.get(slot).contains(value);
    }

    /**
     * Returns the cached candidates for the given slot.
     *
     * @param slot the slot to give candidates for
     * @return the cached candidates for the given slot
     */
    Collection<ValueT> candidates(final SlotT slot) {
        return Collections.unmodifiableCollection(current.get(slot));
    }

    /**
     * Invalidate cache candidates.
     * <p>
     * Cache will be reset to its initial state.
     */
    void invalidateCache(final SlotT unassignedSlot, final ValueT unassignedValue) {
        for (final Map.Entry<SlotT, Collection<ValueT>> entry : current.entrySet()) {
            final SlotT slot = entry.getKey();
            final Collection<ValueT> candidates = entry.getValue();
            initial.get(slot).stream().filter(isCompatibleWith(slot)).forEach(candidates::add);
        }
    }

    /**
     * Update cache given an assignment.
     * <p>
     * This will have the effect to narrow the cache. The more the cache is updated without
     * {@link #invalidateCache(SlotT, ValueT) invalidation}, the smaller the cache will be, the
     * faster the results
     * will be.
     *
     * @param refreshNeeded the predicate that specifies which slot should be re-evaluated
     */
    void updateCache(final Predicate<SlotT> refreshNeeded) {
        for (final Map.Entry<SlotT, Collection<ValueT>> entry : current.entrySet()) {
            final SlotT slot = entry.getKey();
            if (refreshNeeded.test(slot)) {
                final Collection<ValueT> candidates = entry.getValue();
                candidates.removeIf(not(isCompatibleWith(slot)));
            }
        }
    }

    private Predicate<ValueT> isCompatibleWith(final SlotT slot) {
        return word -> eligibility.test(slot, word);
    }
}
