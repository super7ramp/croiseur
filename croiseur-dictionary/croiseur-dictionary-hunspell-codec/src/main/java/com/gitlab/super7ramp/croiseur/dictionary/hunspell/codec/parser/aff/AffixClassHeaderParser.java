/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec.parser.aff;

import com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec.model.aff.AffixClassHeader;
import com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec.model.aff.AffixKind;
import com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec.model.common.Flag;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses {@link AffixClassHeader}.
 */
final class AffixClassHeaderParser {

    /** The pattern of an affix header. */
    private static final Pattern PATTERN = Pattern.compile("^(?<kind>(PFX|SFX)) +" +
            "(?<flag>[^ /]+) +" +
            "(?<crossProduct>[YN]) +" +
            "(?<numberOfRules>[0-9]+)$");

    /**
     * Private constructor to prevent instantiation, static methods only.
     */
    private AffixClassHeaderParser() {
        // Nothing to do.
    }

    /**
     * Parse an affix header line.
     *
     * @param line the line to parse
     * @return the {@link AffixClassHeader}
     */
    static AffixClassHeader parse(final String line) {
        final Matcher matcher = PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Not an affix header: " + line);
        }

        final AffixKind kind = AffixKind.valueOf(matcher.group("kind"));
        final Flag flag = new Flag(matcher.group("flag"));
        final boolean crossProduct = "Y".equals(matcher.group("crossProduct"));
        final int numberOfRules = Integer.parseInt(matcher.group("numberOfRules"));

        return new AffixClassHeader(kind, flag, crossProduct, numberOfRules);
    }
}
