package com.gitlab.super7ramp.crosswords.solver.lib.db;

import com.gitlab.super7ramp.crosswords.solver.lib.core.AdaptedDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.grid.SlotIdentifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mock for {@link AdaptedDictionary}.
 */
final class AdaptedDictionaryMock implements AdaptedDictionary {

    /**
     * Words in dictionary.
     */
    private final Set<String> words;

    /**
     * Blacklist.
     */
    private Map<SlotIdentifier, Set<String>> blacklist;

    /**
     * Constructor.
     *
     * @param someWords words to be added to dictionary
     */
    AdaptedDictionaryMock(String... someWords) {
        words = Arrays.stream(someWords).collect(Collectors.toUnmodifiableSet());
        blacklist = new HashMap<>();
    }

    @Override
    public Set<String> findPossibleValues(final Slot wordVariable) {
        return words.stream()
                .filter(word -> wordVariable.isCompatibleWith(word) && !isBlacklisted(wordVariable.uid(), word))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public long countPossibleValues(final Slot wordVariable) {
        return findPossibleValues(wordVariable).size();
    }

    @Override
    public void blacklist(final SlotIdentifier wordVariable, final String value) {
        blacklist.computeIfAbsent(wordVariable, key -> new HashSet<>());
        blacklist.get(wordVariable).add(value);
    }

    @Override
    public void resetBlacklist() {
        blacklist.clear();
    }

    private boolean isBlacklisted(final SlotIdentifier uid, final String word) {
        return blacklist.getOrDefault(uid, Collections.emptySet()).contains(word);
    }
}
