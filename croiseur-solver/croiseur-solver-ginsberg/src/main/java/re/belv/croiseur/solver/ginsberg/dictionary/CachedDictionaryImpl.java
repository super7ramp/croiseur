/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.dictionary;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.groupingBy;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import re.belv.croiseur.solver.ginsberg.Dictionary;
import re.belv.croiseur.solver.ginsberg.core.Slot;
import re.belv.croiseur.solver.ginsberg.core.SlotIdentifier;
import re.belv.croiseur.solver.ginsberg.elimination.EliminationSpace;

/**
 * Implementation of {@link CachedDictionary}.
 */
final class CachedDictionaryImpl implements CachedDictionaryWriter {

    /** The number of patterns to cache per slot. */
    private static final int CACHED_PATTERNS_PER_SLOT = 1_000;

    /** The initial word candidates per slot. */
    private final Map<SlotIdentifier, Trie> initialCandidates;

    /**
     * Associations between patterns and dictionary words satisfying these patterns. Avoids repeated
     * traversals of {@link #initialCandidates}.
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
    CachedDictionaryImpl(
            final Dictionary dictionary, final Collection<Slot> slots, final EliminationSpace eliminationSpace) {
        els = eliminationSpace;
        initialCandidates = createInitialCandidates(dictionary, slots);
        wordsByPattern = new SizedMap<>(slots.size() * CACHED_PATTERNS_PER_SLOT);
        currentCandidatesCount = new HashMap<>();
    }

    /**
     * Creates the initial candidates tries.
     *
     * @param dictionary the external dictionary
     * @param slots the slots
     * @return the initial candidates tries
     */
    private static Map<SlotIdentifier, Trie> createInitialCandidates(
            final Dictionary dictionary, final Collection<Slot> slots) {

        // Group slot per patterns: Each group of slots will have the same initial candidates.
        final Collection<List<Slot>> slotGroups =
                slots.stream().collect(groupingBy(Slot::asPattern)).values();

        final Map<SlotIdentifier, Trie> tries = new HashMap<>();
        final Iterable<String> words = dictionary.words();
        for (final List<Slot> slotGroup : slotGroups) {
            final Trie trie = new Trie();
            final Slot referenceSlot = slotGroup.get(0);
            for (final String word : words) {
                if (referenceSlot.isCompatibleWith(word)) {
                    trie.add(word);
                }
            }
            for (final Slot slot : slotGroup) {
                tries.put(slot.uid(), trie);
            }
        }
        return tries;
    }

    @Override
    public Stream<String> candidates(final Slot slot) {
        return wordsFromPattern(slot).stream().filter(not(els.eliminatedValues(slot.uid())::contains));
    }

    @Override
    public long cachedCandidatesCount(final Slot slot) {
        return currentCandidatesCount.computeIfAbsent(
                slot.uid(), k -> candidates(slot).count());
    }

    @Override
    public Stream<String> reevaluatedCandidates(final Slot slot) {
        return initialCandidates.get(slot.uid()).streamMatching(slot.asPattern());
    }

    @Override
    public void invalidateCacheCount(final Slot modifiedSlot) {
        currentCandidatesCount.remove(modifiedSlot.uid());
        modifiedSlot.connectedSlots().forEach(slot -> currentCandidatesCount.remove(slot.uid()));
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
        return wordsByPattern.computeIfAbsent(slotPattern, k -> initialCandidates
                .get(slot.uid())
                .streamMatching(slotPattern)
                .toList());
    }
}
