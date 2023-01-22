/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An affix flag type.
 */
public enum FlagType {

    /** Affix name is composed of a single extended-ASCII character. */
    SINGLE_ASCII("[\\x00-\\xFF]"),
    /** Affix name is composed of 2 extended-ASCII characters. */
    LONG_ASCII("[\\x00-\\xFF]{2}"),
    /** Affix name is composed of a number between 1 and 65000. */
    NUMERICAL("[1-9][0-9]{0,4}"),
    /** Affix name is a UTF-8 character. */
    UTF_8(".");

    /** The pattern matching a flag of this flag type. */
    private final Pattern pattern;

    /**
     * Constructs an instance.
     *
     * @param regex the regex matching a flag of this flag type
     */
    FlagType(final String regex) {
        pattern = Pattern.compile(regex);
    }

    /**
     * Returns the default flag type.
     *
     * @return the default flag type
     */
    public static FlagType byDefault() {
        return SINGLE_ASCII;
    }

    /**
     * Splits flag vector according to given flag type.
     *
     * @param flags the flag vector
     * @return the flags as string
     */
    public Collection<Flag> split(final String flags) {
        final Collection<Flag> splitFlags;
        if (flags != null) {
            splitFlags = new ArrayList<>();
            final Matcher matcher = pattern.matcher(flags);
            while (matcher.find()) {
                final Flag flag = new Flag(matcher.group());
                splitFlags.add(flag);
            }
        } else {
            splitFlags = Collections.emptyList();
        }
        return splitFlags;
    }
}
