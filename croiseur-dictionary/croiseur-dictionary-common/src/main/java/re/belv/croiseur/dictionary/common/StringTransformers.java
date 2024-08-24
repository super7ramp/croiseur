/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.common;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Common transformations performed on dictionary entries so that it suits crossword usage.
 */
public final class StringTransformers {

    /** Matches accents. */
    private static final Pattern ACCENTS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    private static final StringTransformer REMOVE_ACCENTUATION = (final String in) -> {
        final String normalized = Normalizer.normalize(in, Normalizer.Form.NFD);
        return ACCENTS.matcher(normalized).replaceAll("");
    };

    /** Matches punctuation. */
    private static final Pattern PUNCTUATION = Pattern.compile("\\p{Punct}");

    private static final StringTransformer REMOVE_PUNCTUATION =
            in -> PUNCTUATION.matcher(in).replaceAll("");

    private static final StringTransformer REMOVE_BLANKS = in -> in.replace(" ", "");

    /**
     * Private constructor to prevent instantiation.
     */
    private StringTransformers() {
        // Nothing to do.
    }

    /**
     * Returns a {@link StringTransformer} which replaces accentuated characters by non-accentuated
     * forms.
     *
     * @return a {@link StringTransformer} which replaces accentuated characters by * non-accentuated
     * forms
     */
    public static StringTransformer removeAccentuation() {
        return REMOVE_ACCENTUATION;
    }

    /**
     * Returns a {@link StringTransformer} which removes punctuation from input string.
     *
     * @return a {@link StringTransformer} which removes punctuation from input string
     */
    public static StringTransformer removePunctuation() {
        return REMOVE_PUNCTUATION;
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
     * Returns a {@link StringTransformer} which converts all the characters in the input String to
     * upper case.
     *
     * @return a {@link StringTransformer} which converts all the characters in the input String to
     * upper case.
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
        return s -> removeBlanks()
                .andThen(removeAccentuation())
                .andThen(removePunctuation())
                .andThen(toUpperCase())
                .apply(s);
    }
}
