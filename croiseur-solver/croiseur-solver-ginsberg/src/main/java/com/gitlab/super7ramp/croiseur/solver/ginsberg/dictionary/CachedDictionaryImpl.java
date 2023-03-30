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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

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
    public Stream<String> candidates(final Slot slot) {
        return currentCandidates.get(slot.uid()).stream();
    }

    @Override
    public long candidatesCount(final Slot slot) {
        return currentCandidates.get(slot.uid()).size();
    }

    @Override
    public Stream<String> refinedCandidates(final Slot slot) {
        final Stream<String> refinedCandidates;
        if (!slot.isInstantiated()) {
            refinedCandidates = currentCandidates.get(slot.uid())
                                                 .stream()
                                                 .filter(slot::isCompatibleWith);
        } else {
            refinedCandidates = Stream.of(slot.value().orElseThrow());
        }
        return refinedCandidates;
    }

    @Override
    public Stream<String> reevaluatedCandidates(final Slot slot) {
        return initialCandidates.get(slot.uid())
                                .streamMatching(slot.asPattern());
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
