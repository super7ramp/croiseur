/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.common.Flag;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Compound flag parser.
 */
final class CompoundFlagParser {

    /** The textual representation. */
    private static final Pattern PATTERN = Pattern.compile("^COMPOUNDFLAG +(?<identifier>[^ ])+$");

    /**
     * Private constructor to prevent instantiation.
     */
    private CompoundFlagParser() {
        // Nothing to do.
    }

    /**
     * Parses a compound flag.
     *
     * @param line the line to parse
     * @return the {@link CompoundFlagParser}
     */
    static Flag parse(final String line) {
        final Matcher matcher = PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Not a compound flag: " + line);
        }
        final String identifier = matcher.group("identifier");
        return new Flag(identifier);
    }
}
