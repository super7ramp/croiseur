package com.gitlab.super7ramp.crosswords.solver.lib.dictionary;

import com.gitlab.super7ramp.crosswords.solver.api.Dictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.InternalDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.core.SlotIdentifier;
import com.gitlab.super7ramp.crosswords.solver.lib.util.SingleElementHashMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Implementation of {@link InternalDictionary}.
 */
public class CachedDictionary implements InternalDictionary {

    /** Actual dictionary. */
    private final Dictionary externalDictionary;

    /**
     * Contains the first result of {@link Dictionary#lookup(Predicate)} for each {@link SlotIdentifier}.
     * <p>
     * It serves as a cache to avoid filtering all the dictionary again. It is assumed that subsequent requests will be
     * more restrictive than the first lookup.
     */
    private final Map<SlotIdentifier, Set<String>> cache;

    /** Blacklist. */
    private final Map<SlotIdentifier, Set<String>> blacklist;

    /** List of already used words. */
    private final Set<String> used;

    /**
     * Constructor.
     *
     * @param aDictionary a dictionary
     */
    public CachedDictionary(final Dictionary aDictionary) {
        externalDictionary = aDictionary;
        cache = new HashMap<>();
        blacklist = new SingleElementHashMap<>();
        used = new HashSet<>();
    }

    @Override
    public Stream<String> findPossibleValues(final Slot wordVariable) {
        final Set<String> cached = cache.get(wordVariable.uid());
        if (cached == null) {
            return searchAndCache(wordVariable);
        }
        return search(cached, wordVariable);
    }

    @Override
    public long countPossibleValues(final Slot wordVariable) {
        return findPossibleValues(wordVariable).count();
    }

    @Override
    public boolean contains(final String value) {
        return externalDictionary.contains(value);
    }

    @Override
    public void use(final String value) {
        used.add(value);
    }

    @Override
    public void free(final String value) {
        used.remove(value);
    }

    @Override
    public void blacklist(final Slot wordVariable, final String value) {
        blacklist.computeIfAbsent(wordVariable.uid(), key -> new HashSet<>()).add(value);
    }

    private Stream<String> search(final Set<String> input, final Slot wordVariable) {
        return input.stream().filter(isCompatibleWith(wordVariable));
    }

    private Stream<String> searchAndCache(final Slot wordVariable) {
        // Use ordered TreeSet for reproducibility
        final Set<String> result = new TreeSet<>(externalDictionary.lookup(isCompatibleWith(wordVariable)));
        cache.put(wordVariable.uid(), result);
        return result.stream();
    }

    private Predicate<String> isCompatibleWith(final Slot slot) {
        return word -> slot.isCompatibleWith(word) &&
                !isBlacklisted(slot.uid(), word) &&
                !isAlreadyUsed(word);
    }

    private boolean isBlacklisted(final SlotIdentifier uid, final String word) {
        return blacklist.getOrDefault(uid, Collections.emptySet()).contains(word);
    }

    private boolean isAlreadyUsed(final String word) {
        return used.contains(word);
    }
}
