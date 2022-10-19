package com.gitlab.super7ramp.crosswords.dictionary.common;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Common transformations performed on dictionary entries.
 */
public final class Transformers {

    /** Matches accents. */
    private static final Pattern ACCENTS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    private static final String HYPHEN = "-";

    private static final String EMPTY = "";

    /**
     * Constructor.
     */
    private Transformers() {
        // Nothing to do.
    }

    /**
     * Removes hyphens from given entry.
     *
     * @param in the entry
     * @return the entry stripped from its hyphens
     */
    public static String removeHyphen(final String in) {
        return in.replace(HYPHEN, EMPTY);
    }

    /**
     * Removes accents from given entry.
     *
     * @param in the entry
     * @return the entry stripped from its accents
     */
    public static String removeAccentuation(final String in) {
        final String normalized = Normalizer.normalize(in, Normalizer.Form.NFD);
        return ACCENTS.matcher(normalized).replaceAll(EMPTY);
    }

    /**
     * Removes accents from given entry.
     *
     * @param in the entry
     * @return the entry stripped from its accents
     */
    public static String removeApostrophe(final String in) {
        return in.replace("'", "");
    }
}
