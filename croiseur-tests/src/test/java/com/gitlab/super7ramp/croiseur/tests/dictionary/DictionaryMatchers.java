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
import java.util.Set;

import static org.mockito.ArgumentMatchers.argThat;

/**
 * Allows creating custom {@link ArgumentMatcher}s related to dictionary presentation.
 */
final class DictionaryMatchers {

    /**
     * Matches a {@link DictionaryContent} with the given number of entries and the given first
     * entries.
     *
     * @param numberOfEntries the expected number of entries
     * @param firstEntries    the expected first entries
     */
    private record DictionaryContentMatcher(int numberOfEntries,
                                            List<String> firstEntries) implements
            ArgumentMatcher<DictionaryContent> {

        @Override
        public boolean matches(final DictionaryContent actual) {
            final Set<String> actualEntries = actual.words();
            return actualEntries.size() == numberOfEntries && firstEntries(actualEntries).equals(firstEntries);
        }

        /**
         * Returns the first entries of this (expected sequenced) Set as a List.
         *
         * @param set the set to take the first entries from
         * @return the first entries
         */
        private List<String> firstEntries(final Set<String> set) {
            return set.stream().limit(firstEntries.size()).toList();
        }
    }

    /**
     * Matches a {@link DictionarySearchResult} with the given number of entries and the given first
     * entries.
     *
     * @param numberOfEntries the expected number of entries
     * @param firstEntries    the expected first entries
     */
    private record DictionarySearchResultMatcher(int numberOfEntries,
                                                 List<String> firstEntries) implements
            ArgumentMatcher<DictionarySearchResult> {

        @Override
        public boolean matches(final DictionarySearchResult actual) {
            final List<String> actualEntries = actual.words();
            System.out.println(actualEntries.size());
            return actualEntries.size() == numberOfEntries && firstEntries(actualEntries).equals(firstEntries);
        }

        /**
         * Returns the first entries of this (expected sequenced) Set as a List.
         *
         * @param set the set to take the first entries from
         * @return the first entries
         */
        private List<String> firstEntries(final Collection<String> set) {
            return set.stream().limit(firstEntries.size()).toList();
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
    static DictionaryContent dictionaryContentOf(final int numberOfEntries,
                                                 final List<String> firstEntries) {
        return argThat(new DictionaryContentMatcher(numberOfEntries, firstEntries));
    }

    /**
     * Allows creating a {@link DictionarySearchResult} matcher.
     *
     * @param numberOfEntries the expected number of entries
     * @param firstEntries    the expected first entries
     * @return {@code null}
     */
    static DictionarySearchResult dictionarySearchResultOf(final int numberOfEntries,
                                                           final List<String> firstEntries) {
        return argThat(new DictionarySearchResultMatcher(numberOfEntries, firstEntries));
    }
}
