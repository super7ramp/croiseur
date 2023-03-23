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
import java.util.function.BiPredicate;
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
        final BiPredicate<SlotIdentifier, String> eligibility =
                (slotId, word) -> !eliminationSpace.eliminatedValues(slotId).contains(word);
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
    private static DictionaryCache<SlotIdentifier> populateCache(final Dictionary dictionary,
                                                                 final Collection<Slot> slots,
                                                                 final BiPredicate<SlotIdentifier
                                                                         , String> eligibility) {
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

    private static Predicate<String> notEliminated(final EliminationSpace els,
                                                   final SlotIdentifier slotId) {
        final Set<String> eliminatedValues = els.eliminatedValues(slotId);
        return not(eliminatedValues::contains);
    }

    @Override
    public Stream<String> candidates(final Slot wordVariable) {
        final Predicate<String> notEliminated = notEliminated(wordVariable.uid());
        return cache.candidates(wordVariable.uid(), wordVariable.asPattern())
                    .filter(notEliminated);
    }

    @Override
    public long candidatesCount(final Slot wordVariable) {
        return cache.cachedCount(wordVariable.uid());
    }

    @Override
    public long refinedCandidatesCount(final Slot wordVariable,
                                       final SlotIdentifier modified) {
        final long count;
        if (wordVariable.isConnectedTo(modified)) {
            final Predicate<String> notEliminated = notEliminated(wordVariable.uid());
            count = cache.candidates(wordVariable.uid(), wordVariable.asPattern())
                         .filter(notEliminated)
                         .count();
        } else {
            count = cache.cachedCount(wordVariable.uid());
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

        final Predicate<String> notEliminated = not(refreshedEliminations.keySet()::contains);
        return cache.candidates(wordVariable.uid(), wordVariable.asPattern())
                    .filter(notEliminated)
                    .count();
    }

    @Override
    public void invalidateCache(final Slot unassignedSlot) {
        final Predicate<String> notEliminated = notEliminated(unassignedSlot.uid());
        cache.invalidateCache(unassignedSlot.uid(), unassignedSlot.asPattern(), notEliminated);
        unassignedSlot.connectedSlots()
                      .forEach(connectedSlot -> cache.invalidateCache(connectedSlot.uid(),
                              connectedSlot.asPattern(), notEliminated));
    }

    @Override
    public void updateCache(final Slot assignedSlot) {
        final Predicate<String> notEliminated = notEliminated(assignedSlot.uid());
        cache.updateCache(assignedSlot.uid(), assignedSlot.asPattern(), notEliminated);
        assignedSlot.connectedSlots()
                    .forEach(connectedSlot -> cache.updateCache(connectedSlot.uid(),
                            connectedSlot.asPattern(), notEliminated));
    }

    private Predicate<String> notEliminated(final SlotIdentifier slotId) {
        return notEliminated(els, slotId);
    }
}
