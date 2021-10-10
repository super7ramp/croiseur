package com.gitlab.super7ramp.crosswords.solver.lib.db;

import com.gitlab.super7ramp.crosswords.solver.api.Dictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.AdaptedDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.grid.SlotIdentifier;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of {@link AdaptedDictionary}.
 */
public class AdaptedDictionaryImpl implements AdaptedDictionary {

    /**
     * Actual dictionary.
     */
    private final Dictionary dictionary;

    /**
     * Blacklist.
     */
    private Map<SlotIdentifier, Set<String>> blacklist;

    /**
     * Constructor.
     *
     * @param aDictionary a dictionary
     */
    public AdaptedDictionaryImpl(final Dictionary aDictionary) {
        dictionary = aDictionary;
    }

    @Override
    public Set<String> findPossibleValues(final Slot wordVariable) {
        return dictionary.lookup(
                word -> wordVariable.isCompatibleWith(word) && !isBlacklisted(wordVariable.uid(), word));
    }

    @Override
    public long countPossibleValues(final Slot wordVariable) {
        return dictionary.countMatches(wordVariable::isCompatibleWith);
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
