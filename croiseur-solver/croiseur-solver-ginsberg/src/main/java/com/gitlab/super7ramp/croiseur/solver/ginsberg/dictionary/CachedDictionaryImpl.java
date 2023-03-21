/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.dictionary;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.Dictionary;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.SlotIdentifier;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.elimination.EliminationSpace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    /** The elimination space. */
    private final EliminationSpace els;

    /**
     * Constructor.
     *
     * @param dictionary a dictionary
     * @param slots      the slots
     */
    CachedDictionaryImpl(final Dictionary dictionary, final Collection<Slot> slots,
                         final EliminationSpace eliminationSpace) {
        els = eliminationSpace;
        eligibility = (slot, word) -> slot.isCompatibleWith(word) &&
                !eliminationSpace.eliminatedValues(slot.uid()).contains(word);
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
    public long refinedCandidatesCount(final Slot wordVariable,
                                       final SlotIdentifier modified) {
        final Collection<String> candidates = cache.candidates(wordVariable);
        final long count;
        if (wordVariable.isConnectedTo(modified)) {
            count = candidates.stream()
                              .filter(word -> eligibility.test(wordVariable, word))
                              .count();
        } else {
            count = candidates.size();
        }
        return count;
    }

    @Override
    public long reevaluatedCandidatesCount(final Slot wordVariable,
                                           final List<SlotIdentifier> modifiedVariables) {
        // Horrible probe of the elimination space
        final Map<String, Set<SlotIdentifier>> refreshedEliminations =
                new HashMap<>(els.eliminations(wordVariable.uid()));
        final Iterator<Map.Entry<String, Set<SlotIdentifier>>> it = refreshedEliminations.entrySet()
                                                                                         .iterator();
        while (it.hasNext()) {
            final Map.Entry<String, Set<SlotIdentifier>> elimination = it.next();
            final Set<SlotIdentifier> reasons = elimination.getValue();
            if (!Collections.disjoint(reasons, modifiedVariables)) {
                it.remove();
            }
        }
        return cache.initialCandidates(wordVariable)
                    .stream()
                    .filter(word -> wordVariable.isCompatibleWith(word) && !refreshedEliminations.containsKey(word))
                    .count();
    }

    @Override
    public void invalidateCache(final Slot unassignedSlot) {
        cache.invalidateCache(unassignedSlot);
        unassignedSlot.connectedSlots().forEach(cache::invalidateCache);
    }

    @Override
    public void updateCache(final Slot assignedSlot) {
        cache.updateCache(assignedSlot);
        assignedSlot.connectedSlots().forEach(cache::updateCache);
    }

}
