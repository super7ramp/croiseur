/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.clue;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Provides crossword clues.
 */
public interface ClueProvider {

    /**
     * The clue provider's name.
     *
     * @return the clue provider's name
     */
    String name();

    /**
     * A short description of the clue provider.
     *
     * @return a short description of the clue provider
     */
    String description();

    /**
     * Generates a clue for a single word.
     *
     * @param word the word to define
     * @return the clue or {@link Optional#empty()} if word couldn't be defined
     */
    default Optional<String> define(final String word) {
        return Optional.ofNullable(define(Set.of(word)).get(word));
    }

    /**
     * Creates clues for the given words.
     *
     * @param words the words to define
     * @return clues indexed by the defined word; Map may be empty or incomplete
     */
    Map<String, String> define(final Set<String> words);
}
