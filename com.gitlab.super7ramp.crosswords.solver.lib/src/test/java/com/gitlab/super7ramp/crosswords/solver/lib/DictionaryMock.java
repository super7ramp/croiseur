package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.Dictionary;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

final class DictionaryMock implements Dictionary {

    private final Set<String> words;

    DictionaryMock(String... someWords) {
        words = Arrays.stream(someWords).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<String> lookup(Predicate<String> predicate) {
        return words.stream().filter(predicate).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public long countMatches(Predicate<String> predicate) {
        return words.stream().filter(predicate).count();
    }

    @Override
    public boolean contains(String value) {
        return words.contains(value);
    }
}
