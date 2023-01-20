/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.Flag;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.FlagType;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a parsed affix rule.
 *
 * @param kind                the affix rule kind
 * @param flag                the flag which applies this rule
 * @param strippingCharacters characters to strip from base word before applying the affix
 * @param affix               the affix to apply
 * @param continuationClasses the affix classes that can be applied on the affixed word
 * @param condition           pattern that the base word must match to be eligible to the affix rule
 */
public record AffixRule(AffixKind kind, Flag flag, Optional<String> strippingCharacters,
                        String affix, Collection<Flag> continuationClasses,
                        Optional<String> condition) {

    /** The pattern of an affix header. */
    private static final Pattern PATTERN =
            Pattern.compile("^(?<kind>(PFX|SFX)) +" +
                    "(?<flag>[^ /]+) +(0|" +
                    "(?<strippingCharacters>[^ /]+)) +" +
                    "(?<affix>[^ /]+)" +
                    "(/(?<continuationClasses>[^ /]+))? +" +
                    "(\\.|(?<condition>[^ /]+)) *$");

    /**
     * Parses an affix rule, using the default flag type to split the continuation classes.
     *
     * @param line line to parse
     * @return the {@link AffixRule}
     */
    static AffixRule valueOf(final String line) {
        return valueOf(line, FlagType.byDefault());
    }

    /**
     * Parses an affix rule.
     *
     * @param line     line to parse
     * @param flagType the flag type (in order to split the continuation classes)
     * @return the {@link AffixRule}
     */
    static AffixRule valueOf(final String line, final FlagType flagType) {
        final Matcher matcher = PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Not an affix rule: " + line);
        }

        final AffixKind kind = AffixKind.valueOf(matcher.group("kind"));
        final Flag flag = new Flag(matcher.group("flag"));
        final Optional<String> strippingCharacters = Optional.ofNullable(matcher.group(
                "strippingCharacters"));
        final String affix = matcher.group("affix");
        final Collection<Flag> continuationClasses = parseContinuationClasses(matcher, flagType);
        final Optional<String> condition = Optional.ofNullable(matcher.group("condition"));

        return new AffixRule(kind, flag, strippingCharacters, affix, continuationClasses,
                condition);
    }

    private static Collection<Flag> parseContinuationClasses(final Matcher matcher,
                                                             final FlagType flagType) {
        return Optional.ofNullable(matcher.group("continuationClasses"))
                       .map(flags -> Flag.split(flags, flagType))
                       .orElseGet(Collections::emptyList);
    }

}
