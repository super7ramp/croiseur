package com.gitlab.super7ramp.crosswords.common.dictionary;

import java.util.Locale;

/**
 * Key information to identify a dictionary.
 *
 * @param provider dictionary provider
 * @param name dictionary name
 * @param locale dictionary locale
 */
public record DictionaryKey(String provider, String name, Locale locale) {
    // Nothing to add
}