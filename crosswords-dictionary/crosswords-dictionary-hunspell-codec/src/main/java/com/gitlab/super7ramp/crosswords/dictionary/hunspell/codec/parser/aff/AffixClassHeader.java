/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.Flag;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An affix class header as parsed from an ".aff" file.
 *
 * @param kind          the kind of affix (prefix/suffix)
 * @param flag          the affix flag
 * @param crossProduct  whether other affix of different kind can be applied when this affix is
 *                      applied
 * @param numberOfRules the number or rules under this header
 */
public record AffixClassHeader(AffixKind kind, Flag flag, boolean crossProduct, int numberOfRules) {

    /** The pattern of an affix header. */
    private static final Pattern PATTERN = Pattern.compile("^(?<kind>(PFX|SFX)) +" +
            "(?<flag>[^ /]+) +" +
            "(?<crossProduct>[YN]) +" +
            "(?<numberOfRules>[0-9]+)$");

    /**
     * Parse an affix header line.
     *
     * @param line the line to parse
     * @return the {@link AffixClassHeader}
     */
    static AffixClassHeader valueOf(final String line) {
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
