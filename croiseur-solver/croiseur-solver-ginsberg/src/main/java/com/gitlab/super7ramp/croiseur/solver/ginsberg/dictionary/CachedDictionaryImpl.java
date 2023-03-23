/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.dictionary;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.Dictionary;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.SlotIdentifier;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.elimination.EliminationSpace;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

/**
 * Implementation of {@link CachedDictionary}.
 */
final class CachedDictionaryImpl implements CachedDictionaryWriter {

    /** Cached dictionary. */
    private final DictionaryCache<SlotIdentifier> cache;

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
        cache = populateCache(dictionary, slots);
    }

    /**
     * Create the dictionary cache.
     *
     * @param dictionary the external dictionary
     * @param slots      the puzzle's slots
     * @return the created {@link DictionaryCache}
     */
    private static DictionaryCache<SlotIdentifier> populateCache(final Dictionary dictionary,
                                                                 final Collection<Slot> slots) {
        final Map<SlotIdentifier, Trie> initialLookup = new HashMap<>();
        slots.forEach(slot -> initialLookup.put(slot.uid(), new Trie()));
        for (final String word : dictionary.lookup(w -> true)) {
            for (final Slot slot : slots) {
                if (slot.isCompatibleWith(word)) {
                    initialLookup.get(slot.uid()).add(word);
                }
            }
        }
        return new DictionaryCache<>(initialLookup);
    }

    @Override
    public Stream<String> candidates(final Slot wordVariable) {
        return cache.stream(wordVariable.uid());
    }

    @Override
    public long candidatesCount(final Slot wordVariable) {
        return cache.size(wordVariable.uid());
    }

    @Override
    public long refinedCandidatesCount(final Slot wordVariable,
                                       final SlotIdentifier modified) {
        final long count;
        if (!wordVariable.isInstantiated()) {
            final Predicate<String> notEliminated = notEliminated(wordVariable.uid());
            count = cache.initial(wordVariable.uid())
                         .streamMatching(wordVariable.asPattern())
                         .filter(notEliminated)
                         .count();
        } else {
            count = 1L;
        }
        return count;
    }

    @Override
    public long reevaluatedCandidatesCount(final Slot wordVariable,
                                           final List<SlotIdentifier> modifiedVariables) {
        // Probe of the elimination space
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

        final Predicate<String> notEliminated = not(refreshedEliminations.keySet()::contains);
        return cache.initial(wordVariable.uid())
                    .streamMatching(wordVariable.asPattern())
                    .filter(notEliminated)
                    .count();
    }

    @Override
    public void invalidateCache(final Slot unassignedSlot) {
        cache.invalidate(unassignedSlot.uid(), unassignedSlot.asPattern(),
                notEliminated(unassignedSlot.uid()));
        unassignedSlot.connectedSlots()
                      .forEach(connectedSlot -> cache.invalidate(connectedSlot.uid(),
                              connectedSlot.asPattern(), notEliminated(connectedSlot.uid())));
    }

    @Override
    public void updateCache(final Slot assignedSlot) {
        cache.update(assignedSlot.uid(), assignedSlot.asPattern(),
                notEliminated(assignedSlot.uid()));
        assignedSlot.connectedSlots()
                    .forEach(connectedSlot -> cache.update(connectedSlot.uid(),
                            connectedSlot.asPattern(), notEliminated(connectedSlot.uid())));
    }

    /**
     * Helper to filter out eliminated values.
     *
     * @param slotId the slot id
     * @return a filter matching only non-eliminated candidates of given slot
     */
    private Predicate<String> notEliminated(final SlotIdentifier slotId) {
        final Set<String> eliminatedValues = els.eliminatedValues(slotId);
        return not(eliminatedValues::contains);
    }
}
