/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.dictionary;

import java.util.ArrayList;
import java.util.Collection;
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
     * Data necessary to update dictionary cache after invalidation.
     *
     * @param pattern          the new pattern to use
     * @param additionalFilter an additional filter
     */
    private record Invalidation(String pattern, Predicate<String> additionalFilter) {
        // Nothing to add.
    }

    /**
     * Contains the initial candidates for each slot, after lookup in the real dictionary.
     * <p>
     * It serves as a first cache to avoid filtering all the dictionary again. It is assumed that
     * subsequent requests will be more restrictive than the first lookup.
     */
    private final Map<? extends K, Trie> initial;

    /**
     * The cached candidates for each slot. The cached collections are simple lists, which are
     * fast to iterate on.
     */
    private final Map<K, Collection<String>> cached;

    /** The invalidations for each cached slot. */
    private final Map<K, Invalidation> invalidations;

    /**
     * Constructor.
     *
     * @param initialCandidates initial lookup result
     */
    DictionaryCache(final Map<? extends K, Trie> initialCandidates) {
        initial = initialCandidates;
        cached = new HashMap<>(initialCandidates.size());
        initial.forEach(((slotIdentifier, trie) -> cached.put(slotIdentifier,
                new ArrayList<>(trie))));
        invalidations = new HashMap<>(initialCandidates.size());
    }

    /**
     * Returns the initial candidates.
     *
     * @param slotId the id of the slot to give the initial candidates for
     * @return the initial candidates
     */
    Trie initial(final K slotId) {
        return initial.get(slotId);
    }

    /**
     * Returns the cached candidates for the given slot.
     *
     * @param slotId the id of the slot to give candidates for
     * @return the cached candidates for the given slot
     */
    Stream<String> stream(final K slotId) {
        return cached(slotId).stream();
    }

    /**
     * Returns the cached candidate count for the given slot id.
     *
     * @param slotId the slot id
     * @return the current candidate count
     */
    int size(final K slotId) {
        return cached(slotId).size();
    }

    /**
     * Invalidate candidates cache for given slot id.
     * <p>
     * Cache will be reset to its initial state then re-evaluated given current slot state.
     *
     * @param slotId           the invalidated slot id
     * @param newSlotPattern   the new slot pattern
     * @param additionalFilter an additional filter on slot
     */
    void invalidate(final K slotId, final String newSlotPattern,
                    final Predicate<String> additionalFilter) {
        invalidations.put(slotId, new Invalidation(newSlotPattern, additionalFilter));
    }

    /**
     * Updates cache given an assignment.
     * <p>
     * This will have the effect to narrow the cache. The more the cache is updated without
     * {@link #invalidate invalidation}, the smaller the cache will be, the faster the
     * results will be.
     *
     * @param slotId           the updated slot id
     * @param newSlotPattern   the new slot pattern
     * @param additionalFilter an additional filter on slot
     */
    void update(final K slotId, final String newSlotPattern,
                final Predicate<String> additionalFilter) {
        // TODO Actually reduce the cache instead of invalidating and rebuilding it entirely
        //  Ideally, create a kind of removeNonMatching(String pattern) on Trie and use Trie as
        //  cached collection. As of now, there is no point to filter on a simple list, it is
        //  slower than rebuilding it from a filtered Trie Stream.
        invalidate(slotId, newSlotPattern, additionalFilter);
    }

    /**
     * Gets the cached data for given slot id.
     *
     * @param slotId the slot id
     * @return the cached data for given slot id
     */
    private Collection<String> cached(final K slotId) {
        final Collection<String> result = cached.get(slotId);
        final Invalidation invalidation = invalidations.remove(slotId);
        if (invalidation != null) {
            result.clear();
            initial.get(slotId)
                   .streamMatching(invalidation.pattern)
                   .filter(invalidation.additionalFilter)
                   .forEach(result::add);
        }
        return result;
    }
}
