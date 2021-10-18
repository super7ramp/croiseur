package com.gitlab.super7ramp.crosswords.solver.lib.db;

import com.gitlab.super7ramp.crosswords.solver.api.Dictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.AdaptedDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.grid.SlotIdentifier;

import java.util.*;

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
    private final Map<SlotIdentifier, Set<String>> blacklist;

    /**
     * Lock list.
     */
    private final Set<String> locklist;

    /**
     * Constructor.
     *
     * @param aDictionary a dictionary
     */
    public AdaptedDictionaryImpl(final Dictionary aDictionary) {
        dictionary = aDictionary;
        blacklist = new HashMap<>();
        locklist = new HashSet<>();
    }

    @Override
    public Set<String> findPossibleValues(final Slot wordVariable) {
        return dictionary.lookup(
                word -> wordVariable.isCompatibleWith(word) &&
                        !isBlacklisted(wordVariable.uid(), word) &&
                        !isAlreadyUsed(word));
    }

    @Override
    public long countPossibleValues(final Slot wordVariable) {
        return dictionary.countMatches(wordVariable::isCompatibleWith);
    }

    @Override
    public boolean contains(String value) {
        return dictionary.contains(value);
    }

    @Override
    public void lock(final String value) {
        locklist.add(value);
    }

    @Override
    public void unlock(final String value) {
        locklist.remove(value);
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

    private boolean isAlreadyUsed(final String word) {
        return locklist.contains(word);
    }
}
