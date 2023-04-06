/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.tests.dictionary;

import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryContent;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionarySearchResult;
import org.mockito.ArgumentMatcher;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.argThat;

/**
 * Allows creating custom {@link ArgumentMatcher}s related to dictionary presentation.
 */
final class DictionaryMatchers {

    /**
     * Matches an argument with a property matching the given property matcher.
     *
     * @param propertyExtractor the function to extract the property
     * @param propertyMatcher   the property matcher
     * @param <T>               the matched argument type
     * @param <U>               the matched property type
     */
    private record HasProperty<T, U>(Function<T, U> propertyExtractor,
            ArgumentMatcher<U> propertyMatcher) implements ArgumentMatcher<T> {

        @Override
        public boolean matches(final T argument) {
            final U property = propertyExtractor.apply(argument);
            return propertyMatcher.matches(property);
        }

        @Override
        public String toString() {
            return "An object which has the property: " + propertyMatcher;
        }
    }

    /**
     * Matches a Collection of String with the given number of entries and the given first entries.
     *
     * @param size the size of the word collection
     * @param head the beginning of the word collection
     */
    private record HasWords(int size, Collection<String> head) implements
            ArgumentMatcher<Collection<String>> {

        @Override
        public boolean matches(final Collection<String> words) {
            return size == words.size() && head.equals(words.stream().limit(head.size()).toList());
        }

        @Override
        public String toString() {
            return "A collection of " + size + " words, starting with " + head;
        }
    }

    /** Private constructor to prevent instantiation, static utilities only. */
    private DictionaryMatchers() {
        // Nothing to do.
    }

    /**
     * Allows creating a {@link DictionaryContent} matcher.
     *
     * @param numberOfEntries the expected number of entries
     * @param firstEntries    the expected first entries
     * @return {@code null}
     */
    static DictionaryContent dictionaryContentWith(final int numberOfEntries,
                                                   final List<String> firstEntries) {
        final Function<DictionaryContent, Collection<String>> property = DictionaryContent::words;
        final var hasWords = new HasWords(numberOfEntries, firstEntries);
        return hasPropertyThat(property, hasWords);
    }

    /**
     * Allows creating a {@link DictionarySearchResult} matcher.
     *
     * @param numberOfMatches the expected number of matches
     * @param firstMatches    the expected first matches
     * @return {@code null}
     */
    static DictionarySearchResult searchResultWith(final int numberOfMatches,
                                                   final List<String> firstMatches) {
        final Function<DictionarySearchResult, Collection<String>> property =
                DictionarySearchResult::words;
        final var hasWords = new HasWords(numberOfMatches, firstMatches);
        return hasPropertyThat(property, hasWords);
    }

    /**
     * Object that has a property matching the given predicate.
     *
     * @param propertyExtractor the function to extract the property from the tested object
     * @param propertyPredicate the predicate applicable to the extracted property
     * @param <T>               the object type
     * @param <U>               the matched property type
     * @return {@code null}
     */
    private static <T, U> T hasPropertyThat(final Function<T, U> propertyExtractor,
                                            final ArgumentMatcher<U> propertyPredicate) {
        final var hasProperty = new HasProperty<>(propertyExtractor, propertyPredicate);
        return argThat(hasProperty);
    }
}
