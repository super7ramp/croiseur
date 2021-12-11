package com.gitlab.super7ramp.crosswords.dictionary.api;

import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;

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
     * Search for words matching the given {@link Predicate}.
     *
     * @param predicate the predicate to satisfy
     * @return a set of words matching the given pattern
     */
    Set<String> lookup(final Predicate<String> predicate);

    /**
     * Dictionary name.
     *
     * @return the dictionary name
     */
    String name();
}
