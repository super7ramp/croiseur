package com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Refine raw dictionary output to
 */
// TODO assess whether it should be done on caller/solver side instead
final class Transformers {

    private static final Pattern ACCENTS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    private static final String HYPHEN = "-";

    private static final String EMPTY = "";

    /**
     * Constructor.
     */
    private Transformers() {
        // Nothing to do.
    }

    static String removeHyphen(final String in) {
        return in.replace(HYPHEN, EMPTY);
    }

    static String removeAccentuation(final String in) {
        final String normalized = Normalizer.normalize(in, Normalizer.Form.NFD);
        return ACCENTS.matcher(normalized).replaceAll(EMPTY);
    }
}
