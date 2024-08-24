/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.common;

import static java.util.function.Predicate.not;

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
     * Returns a filter suitable to exclude empty dictionary entries.
     *
     * @return a filter suitable to exclude empty dictionary entries
     */
    public static Predicate<String> notEmpty() {
        return not(String::isEmpty);
    }

    /**
     * Returns a filter suitable to exclude dictionary entries containing characters outside range
     * A-Z.
     *
     * @return a filter suitable to exclude dictionary entries containing characters outside range
     * A-Z.
     */
    public static Predicate<String> hasOnlyCharactersInRangeAtoZ() {
        return s -> s.chars().allMatch(c -> c >= 'A' && c <= 'Z');
    }
}
