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

    /** The initial word candidates per slot. */
    private final Map<SlotIdentifier, Trie> initialCandidates;

    /** The current word candidates per slot. */
    private final DictionaryCache<SlotIdentifier> currentCandidates;

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
        initialCandidates = new HashMap<>();
        slots.forEach(slot -> initialCandidates.put(slot.uid(), new Trie()));
        for (final String word : dictionary.lookup(w -> true)) {
            for (final Slot slot : slots) {
                if (slot.isCompatibleWith(word)) {
                    initialCandidates.get(slot.uid()).add(word);
                }
            }
        }
        currentCandidates = new DictionaryCache<>(initialCandidates);
    }

    @Override
    public Stream<String> candidates(final Slot wordVariable) {
        return currentCandidates.get(wordVariable.uid()).stream();
    }

    @Override
    public long candidatesCount(final Slot wordVariable) {
        return currentCandidates.get(wordVariable.uid()).size();
    }

    @Override
    public long refinedCandidatesCount(final Slot wordVariable,
                                       final SlotIdentifier modified) {
        final long count;
        if (!wordVariable.isInstantiated()) {
            count = currentCandidates.get(wordVariable.uid())
                                     .stream()
                                     .filter(wordVariable::isCompatibleWith)
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
        return initialCandidates.get(wordVariable.uid())
                                .streamMatching(wordVariable.asPattern())
                                .filter(notEliminated)
                                .count();
    }

    @Override
    public void invalidateCache(final Slot unassignedSlot) {
        currentCandidates.invalidate(unassignedSlot.uid(), unassignedSlot.asPattern(),
                els.eliminatedValues(unassignedSlot.uid()));
        unassignedSlot.connectedSlots()
                      .forEach(slot -> currentCandidates.invalidate(slot.uid(), slot.asPattern(),
                              els.eliminatedValues(slot.uid())));
    }

    @Override
    public void updateCache(final Slot assignedSlot) {
        currentCandidates.narrow(assignedSlot.uid(), assignedSlot::isCompatibleWith);
        assignedSlot.connectedSlots()
                    .forEach(slot -> currentCandidates.narrow(slot.uid(), slot::isCompatibleWith));
    }

}
