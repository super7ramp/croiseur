/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.dictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.function.Predicate.not;

/**
 * Dictionary cache.
 *
 * @param <K> the key type
 */
final class DictionaryCache<K> {

    /**
     * Data necessary to update dictionary cache after invalidation.
     *
     * @param pattern   the new pattern to use
     * @param blacklist an additional filter on slot
     */
    private record Invalidation(String pattern, Collection<String> blacklist) {
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
     * The current candidates for each slot.
     * <p>
     * Values are frequently modified upon assignments/unassignments. Values are array lists
     * rather than tries because array lists are faster to modify and to iterate than tries.
     */
    private final Map<K, Collection<String>> current;

    /** The invalidations for each cached slot. */
    private final Map<K, Invalidation> invalidations;

    /**
     * Constructor.
     *
     * @param initialCandidates initial lookup result
     */
    DictionaryCache(final Map<? extends K, Trie> initialCandidates) {
        initial = initialCandidates;
        current = new HashMap<>(initialCandidates.size());
        initial.forEach(((slotId, trie) -> current.put(slotId, new ArrayList<>(trie))));
        invalidations = new HashMap<>(initialCandidates.size());
    }

    /**
     * Returns the current candidates.
     *
     * @param slotId the id of the slot to give the current candidates for
     * @return the current candidates
     */
    Collection<String> get(final K slotId) {
        return Collections.unmodifiableCollection(current(slotId));
    }

    /**
     * Invalidate the current candidates cache for given slot id.
     * <p>
     * Cache will be reset to its initial state then re-evaluated given new slot pattern and
     * blacklist.
     *
     * @param slotId         the invalidated slot id
     * @param newSlotPattern the new slot pattern
     * @param blacklist      an additional filter on slot
     */
    void invalidate(final K slotId, final String newSlotPattern,
                    final Collection<String> blacklist) {
        invalidations.put(slotId, new Invalidation(newSlotPattern, blacklist));
    }

    /**
     * Narrows current candidates according to given predicate.
     * <p>
     * The more the cache is updated without {@link #invalidate invalidation}, the smaller the
     * cache will be, the faster the results will be.
     *
     * @param slotId          the updated slot id
     * @param narrowPredicate the predicate that words must now satisfy
     */
    void narrow(final K slotId, final Predicate<String> narrowPredicate) {
        current(slotId).removeIf(not(narrowPredicate));
    }

    /**
     * Gets the current cached data for given slot id, recreating data from initial dictionary if
     * cache has been invalidated.
     *
     * @param slotId the slot id
     * @return the cached data for given slot id
     */
    private Collection<String> current(final K slotId) {
        final Collection<String> result = current.get(slotId);
        final Invalidation invalidation = invalidations.remove(slotId);
        if (invalidation != null) {
            result.clear();
            initial.get(slotId)
                   .streamMatching(invalidation.pattern)
                   .filter(not(invalidation.blacklist::contains))
                   .forEach(result::add);
        }
        return result;
    }
}
