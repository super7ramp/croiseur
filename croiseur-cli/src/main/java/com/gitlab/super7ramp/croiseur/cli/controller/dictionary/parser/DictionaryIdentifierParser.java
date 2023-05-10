/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.controller.dictionary.parser;

import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryIdentifier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for {@link DictionaryIdentifier}.
 */
public final class DictionaryIdentifierParser {

    /** Textual representation pattern: providerName:dictionaryName. */
    private static final Pattern PATTERN = Pattern.compile("((?<provider>[^:]+):)" +
                                                           "(?<dictionary>[^:]+)");

    /** Private constructor to prevent instantiation. */
    private DictionaryIdentifierParser() {
        // Nothing to do.
    }

    /**
     * Create a new {@link DictionaryIdentifier} from its textual representation.
     *
     * @param text the textual representation
     * @return the value
     */
    public static DictionaryIdentifier parse(final String text) {
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
