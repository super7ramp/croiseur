package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.Dictionary;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Mock for {@link Dictionary}.
 */
final class DictionaryMock implements Dictionary {

    private final Set<String> words;

    /**
     * Constructor.
     *
     * @param someWords words of the dictionary
     */
    DictionaryMock(final String... someWords) {
        words = Arrays.stream(someWords).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<String> lookup(final Predicate<String> predicate) {
        return words.stream().filter(predicate).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public long countMatches(final Predicate<String> predicate) {
        return words.stream().filter(predicate).count();
    }

    @Override
    public boolean contains(final String value) {
        return words.contains(value);
    }
}
