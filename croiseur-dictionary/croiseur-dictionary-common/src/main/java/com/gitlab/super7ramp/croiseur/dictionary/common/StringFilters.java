/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.common;

import java.util.function.Predicate;

/**
 * Common filters applied on dictionaries.
 */
public final class StringFilters {

    /**
     * Private constructor, static utilities only.
     */
    private StringFilters() {
        // Nothing to do.
    }

    /**
     * Returns a filter suitable to exclude single-letter dictionary entries.
     *
     * @return a filter suitable to exclude single-letter dictionary entries
     */
    public static Predicate<String> atLeastTwoCharacters() {
        return s -> s.length() >= 2;
    }
}
