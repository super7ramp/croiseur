/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.dic;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.common.Flag;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.dic.DicEntry;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.FlagType;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses {@link DicEntry}.
 */
final class DicEntryParser {

    /** The pattern of a dictionary entry. */
    private static final Pattern PATTERN = Pattern.compile("^(?<forbidden>\\*)?" +
            "(?<word>[^ /\t]+)" +
            "(/(?<flags>[^ /\t]+))?" +
            "([\t| ]+(?<morphology>.+))?" +
            "[ \t]*$"); // trailing spaces or tabs should not cause parsing failure

    /**
     * Private constructor to prevent instantiation, static methods only.
     */
    private DicEntryParser() {
        // Nothing to do.
    }

    /**
     * Parses a {@link DicEntry}.
     *
     * @param line the line to parse
     * @return a {@link DicEntry}
     * @throws InvalidDicEntryException if parsing goes wrong
     */
    static DicEntry parse(final String line) throws InvalidDicEntryException {
        return parse(line, FlagType.SINGLE_ASCII);
    }

    /**
     * Parses a {@link DicEntry}.
     *
     * @param line     the line to parse
     * @param flagType the flag type
     * @return a {@link DicEntry}
     * @throws InvalidDicEntryException if parsing goes wrong
     */
    static DicEntry parse(final String line, final FlagType flagType) throws InvalidDicEntryException {

        final Matcher matcher = PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new InvalidDicEntryException("Not a dic entry: " + line);
        }

        final boolean isForbidden = matcher.group("forbidden") != null;
        final String word = matcher.group("word");
        final Collection<Flag> flags = flagType.split(matcher.group("flags"));

        return new DicEntry(isForbidden, word, flags);
    }
}
