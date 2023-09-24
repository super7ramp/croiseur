/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.common;

import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;

import static java.util.function.Predicate.not;

/**
 * Common filters applied on dictionaries.
 */
public final class StringFilters {

    /** US-ASCII encoder. */
    private static final CharsetEncoder US_ASCII_ENCODER = StandardCharsets.US_ASCII.newEncoder();

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
     * Returns a filter suitable to exclude dictionary entries containing non-ASCII characters.
     *
     * @return a filter suitable to exclude dictionary entries containing non-ASCII characters.
     */
    public static Predicate<String> isAscii() {
        return US_ASCII_ENCODER::canEncode;
    }
}
