package com.gitlab.super7ramp.crosswords.db.api;

import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public interface Dictionary {

    /**
     * The {@link Locale} associated to this dictionary.
     * <p>
     * TODO proper noun: Locale-independent?
     * TODO more than one locale per dictionary?
     *
     * @return the {@link Locale} associated to this dictionary
     */
    Locale locale();

    /**
     * Search for words matching the given {@link Pattern}.
     *
     * @param pattern the pattern to search
     * @return a set of words matching the given pattern
     */
    Set<String> lookup(final Pattern pattern);

    /**
     * Count matches for given {@link Pattern}.
     *
     * @param pattern the pattern for which to count matches
     * @return the number of matches for given pattern
     */
    long countMatches(final Pattern pattern);
}
