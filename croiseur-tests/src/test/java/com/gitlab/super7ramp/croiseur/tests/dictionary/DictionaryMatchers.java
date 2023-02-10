/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.tests.dictionary;

import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryContent;
import org.mockito.ArgumentMatcher;

import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;

/**
 * Allows creating custom {@link ArgumentMatcher}s related to dictionary presentation.
 */
public final class DictionaryMatchers {

    /**
     * Matches a {@link DictionaryContent} with the given number of entries and the given first
     * entries.
     *
     * @param numberOfEntries the expected number of entries
     * @param firstEntries    the expected first entries
     */
    private record DictionaryContentMatcher(int numberOfEntries,
                                            List<String> firstEntries) implements ArgumentMatcher<DictionaryContent> {

        @Override
        public boolean matches(final DictionaryContent actual) {
            final List<String> actualEntries = actual.words();
            return actualEntries.size() == numberOfEntries &&
                    actualEntries.subList(0, firstEntries.size()).equals(firstEntries);
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
}
