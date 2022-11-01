package com.gitlab.super7ramp.crosswords.spi.dictionary;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

/**
 * A dictionary.
 */
public interface Dictionary extends DictionaryDescription {

    /**
     * Returns the dictionary as a stream of String.
     *
     * @return the dictionary as a stream of String
     */
    Stream<String> stream();

    /**
     * Searches for words matching the given {@link Predicate}.
     *
     * @param predicate the predicate to satisfy
     * @return a set of words matching the given pattern
     */
    default Set<String> lookup(final Predicate<String> predicate) {
        return stream().filter(predicate).collect(toSet());
    }


}
