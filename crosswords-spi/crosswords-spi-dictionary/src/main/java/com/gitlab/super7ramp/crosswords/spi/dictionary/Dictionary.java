package com.gitlab.super7ramp.crosswords.spi.dictionary;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryDescription;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

/**
 * A dictionary.
 */
public interface Dictionary {

    /**
     * Returns a description of this dictionary.
     *
     * @return a description of this dictionary
     */
    DictionaryDescription description();

    /**
     * Returns the dictionary as a stream of strings.
     *
     * @return the dictionary as a stream of strings
     * @implSpec the returned strings:
     * <ul>
     *     <li>shall only contain characters which can be represented as a primitive char value
     *     (a single 16-bit Unicode character, range {@code u0000} to {@code uFFFF} inclusive)
     *     <li>shall not contain the character '\u0020' (space)
     *     <li>shall not contain the character '\u0023' (number sign}
     *     <li>should be unique</li>
     *     <li>should be either all uppercase or all lowercase but not a mix of the two (processing
     *     is case sensitive)</li>
     * </ul>
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
