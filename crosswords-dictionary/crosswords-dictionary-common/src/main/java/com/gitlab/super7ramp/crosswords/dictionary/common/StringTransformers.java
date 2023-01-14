/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.common;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Common transformations performed on dictionary entries so that it suits crossword usage.
 */
public final class StringTransformers {

    /** Matches accents. */
    private static final Pattern ACCENTS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    private static final String DOT = ".";

    private static final String HYPHEN = "-";

    private static final String EMPTY = "";

    private static final StringTransformer REMOVE_HYPHEN = in -> in.replace(HYPHEN, EMPTY);

    private static final StringTransformer REMOVE_ACCENTUATION = (final String in) -> {
        final String normalized =
                Normalizer.normalize(in, Normalizer.Form.NFD);
        return ACCENTS.matcher(normalized).replaceAll(EMPTY);
    };

    private static final StringTransformer REMOVE_DOT = in -> in.replace(DOT, EMPTY);

    private static final StringTransformer REMOVE_APOSTROPHE = in -> in.replace("'", "");

    private static final StringTransformer REMOVE_BLANKS = in -> in.replace(" ", "");


    /**
     * Private constructor to prevent instantiation.
     */
    private StringTransformers() {
        // Nothing to do.
    }

    /**
     * Returns a {@link StringTransformer} which remove hyphens from input string.
     *
     * @return a {@link StringTransformer} which remove hyphens from input string
     */
    public static StringTransformer removeHyphen() {
        return REMOVE_HYPHEN;
    }

    /**
     * Returns a {@link StringTransformer} which replace accentuated characters by
     * non-accentuated forms.
     *
     * @return a {@link StringTransformer} which replace accentuated characters by
     * * non-accentuated forms
     */
    public static StringTransformer removeAccentuation() {
        return REMOVE_ACCENTUATION;
    }

    /**
     * Returns a {@link StringTransformer} which removes dots from input string.
     *
     * @return a {@link StringTransformer} which removes dots from input string
     */
    public static StringTransformer removeDot() {
        return REMOVE_DOT;
    }

    /**
     * Returns a {@link StringTransformer} which removes accents from input string.
     *
     * @return a {@link StringTransformer} which removes accents from input string
     */
    public static StringTransformer removeApostrophe() {
        return REMOVE_APOSTROPHE;
    }

    /**
     * Returns a {@link StringTransformer} which removes blanks from input string.
     *
     * @return a {@link StringTransformer} which removes blanks from input string
     */
    public static StringTransformer removeBlanks() {
        return REMOVE_BLANKS;
    }

    /**
     * Returns a {@link StringTransformer} which converts all the characters in the input String
     * to upper case.
     *
     * @return a {@link StringTransformer} which converts all the characters in the input String
     * to upper case.
     */
    public static StringTransformer toUpperCase() {
        return String::toUpperCase;
    }

    /**
     * Returns a {@link StringTransformer} applying all known transformations so that transformed
     * string is an acceptable crossword entry.
     *
     * @return a {@link StringTransformer} applying all known transformations
     */
    public static StringTransformer toAcceptableCrosswordEntry() {
        return s -> removeBlanks().andThen(removeHyphen())
                                  .andThen(removeApostrophe())
                                  .andThen(removeDot())
                                  .andThen(toUpperCase())
                                  .andThen(removeAccentuation())
                                  .apply(s);
    }
}
