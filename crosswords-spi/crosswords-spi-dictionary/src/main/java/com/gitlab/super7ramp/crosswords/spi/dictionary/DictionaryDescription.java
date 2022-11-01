package com.gitlab.super7ramp.crosswords.spi.dictionary;

import java.util.Locale;

/**
 * Details about a dictionary.
 */
public interface DictionaryDescription {

    /**
     * Returns the dictionary name.
     *
     * @return the dictionary name
     */
    String name();

    /**
     * Returns the {@link Locale} associated to this dictionary.
     * <p>
     * TODO proper noun: Locale-independent?
     * TODO more than one locale per dictionary?
     *
     * @return the {@link Locale} associated to this dictionary
     */
    Locale locale();
}
