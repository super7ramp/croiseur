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
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

/**
 * Implementation of {@link CachedDictionary}.
 */
final class CachedDictionaryImpl implements CachedDictionaryWriter {

    /** The number of patterns to cache per slot. */
    private static final int CACHED_PATTERNS_PER_SLOT = 1_000;

    /** The initial word candidates per slot. */
    private final Map<SlotIdentifier, Trie> initialCandidates;

    /**
     * Associations between patterns and dictionary words satisfying these patterns. Avoids
     * repeated traversals of {@link #initialCandidates}.
     */
    private final Map<String, List<String>> wordsByPattern;

    /** The cache candidates count. Avoids filtering {@link #wordsByPattern} with {@link #els} */
    private final Map<SlotIdentifier, Long> currentCandidatesCount;

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
        wordsByPattern = new SizedMap<>(slots.size() * CACHED_PATTERNS_PER_SLOT);
        currentCandidatesCount = new HashMap<>();
        slots.forEach(slot -> initialCandidates.put(slot.uid(), new Trie()));
        for (final String word : dictionary.words()) {
            for (final Slot slot : slots) {
                if (slot.isCompatibleWith(word)) {
                    initialCandidates.get(slot.uid()).add(word);
                }
            }
        }
    }

    @Override
    public Stream<String> candidates(final Slot slot) {
        return wordsFromPattern(slot).stream()
                                     .filter(not(els.eliminatedValues(slot.uid())::contains));
    }

    @Override
    public long cachedCandidatesCount(final Slot slot) {
        return currentCandidatesCount.computeIfAbsent(slot.uid(),
                k -> candidates(slot).count());
    }

    @Override
    public Stream<String> reevaluatedCandidates(final Slot slot) {
        return initialCandidates.get(slot.uid())
                                .streamMatching(slot.asPattern());
    }

    @Override
    public void invalidateCacheCount(final Slot modifiedSlot) {
        currentCandidatesCount.remove(modifiedSlot.uid());
        modifiedSlot.connectedSlots()
                    .forEach(slot -> currentCandidatesCount.remove(slot.uid()));
    }

    /**
     * Gets the words satisfying the pattern of given slot.
     * <p>
     * Updates {@link #wordsByPattern} cache if necessary.
     *
     * @param slot the slot
     * @return the words satisfying the pattern of given slot
     */
    private List<String> wordsFromPattern(final Slot slot) {
        final String slotPattern = slot.asPattern();
        return wordsByPattern.computeIfAbsent(slotPattern,
                k -> initialCandidates.get(slot.uid()).streamMatching(slotPattern).toList());
    }
}
