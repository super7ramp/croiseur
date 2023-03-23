/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.dictionary;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Dictionary cache.
 *
 * @param <K> the key type
 */
final class DictionaryCache<K> {

    /**
     * Contains the initial candidates for each slot, after lookup in the real dictionary.
     * <p>
     * It serves as a first cache to avoid filtering all the dictionary again. It is assumed that
     * subsequent requests will be more restrictive than the first lookup.
     */
    private final Map<K, Trie> initial;

    /** The current candidates count for each slot. */
    private final Map<K, Integer> cachedCounts;

    /**
     * Constructor.
     *
     * @param initialCandidates initial lookup result
     * @param anEligibility     slot-word eligibility predicate for future updates
     */
    DictionaryCache(final Map<K, Trie> initialCandidates) {
        initial = initialCandidates;
        cachedCounts = new HashMap<>(initialCandidates.size());
        initial.forEach(((slotIdentifier, trie) -> cachedCounts.put(slotIdentifier, trie.size())));
    }

    /**
     * Returns the cached candidates for the given slot.
     *
     * @param slotId the id of the slot to give candidates for
     * @return the cached candidates for the given slot
     */
    Stream<String> candidates(final K slotId, final String pattern) {
        return initial.get(slotId).streamMatching(pattern);
    }

    /**
     * Returns the cached candidate count for the given slot id.
     *
     * @param slotId the slot id
     * @return the current candidate count
     */
    int cachedCount(final K slotId) {
        return cachedCounts.get(slotId);
    }

    /**
     * Invalidate cache candidates.
     * <p>
     * Cache will be reset to its initial state then re-evaluated given current slot state.
     */
    void invalidateCache(final K invalidated, final String newSlotPattern,
                         final Predicate<String> additionalFilter) {
        final long updatedCount = initial.get(invalidated)
                                         .streamMatching(newSlotPattern)
                                         .filter(additionalFilter)
                                         .count();
        cachedCounts.put(invalidated, (int) updatedCount);
    }

    /**
     * Updates cache given an assignment.
     * <p>
     * This will have the effect to narrow the cache. The more the cache is updated without
     * {@link #invalidateCache invalidation}, the smaller the cache will be, the faster the
     * results will be.
     *
     * @param updated the updated slot
     */
    void updateCache(final K updated, final String newSlotPattern,
                     final Predicate<String> additionalFilter) {
        invalidateCache(updated, newSlotPattern, additionalFilter);
    }

}
