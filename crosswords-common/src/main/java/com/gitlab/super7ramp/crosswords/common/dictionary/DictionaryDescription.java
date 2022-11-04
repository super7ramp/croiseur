package com.gitlab.super7ramp.crosswords.common.dictionary;

import java.util.Locale;

/**
 * A dictionary description.
 *
 * @param name   the name of the dictionary
 * @param locale the locale of the dictionary
 */
public record DictionaryDescription(String name, Locale locale) {
    // Nothing to add.
}
