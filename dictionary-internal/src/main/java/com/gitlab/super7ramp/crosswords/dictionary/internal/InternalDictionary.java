package com.gitlab.super7ramp.crosswords.dictionary.internal;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/*
 * Internal dictionary, just a big serializable list of entries.
 */
public record InternalDictionary(Locale locale, List<String> entries) implements Serializable {
    // Nothing to add.
}
