/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.api.dictionary;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Unique identification for a dictionary.
 *
 * @param providerName   the dictionary provider name
 * @param dictionaryName the dictionary name
 */
public record DictionaryIdentifier(String providerName, String dictionaryName) {

    /** Textual representation pattern: providerName:dictionaryName. */
    private static final Pattern PATTERN = Pattern.compile("((?<provider>[^:]+):)" +
            "(?<dictionary>[^:]+)");

    /**
     * Create a new {@link DictionaryIdentifier} from its textual representation.
     *
     * @param text the textual representation
     * @return the value
     */
    public static DictionaryIdentifier valueOf(final String text) {
        final Matcher matcher = PATTERN.matcher(text);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid format: Expected " + PATTERN.pattern() +
                    ", was " + text);
        }
        final String providerName = matcher.group("provider");
        final String dictionaryName = matcher.group("dictionary");
        return new DictionaryIdentifier(providerName, dictionaryName);
    }
}
