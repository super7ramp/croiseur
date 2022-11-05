package com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An affix flag.
 */
public record Flag(String identifier) {

    /** The pattern per flag type. */
    private static final Map<FlagType, Pattern> PATTERNS = Map.of(
            FlagType.SINGLE_ASCII, Pattern.compile("[\\x00-\\xFF]"),
            FlagType.LONG_ASCII, Pattern.compile("[\\x00-\\xFF]{2}"),
            FlagType.NUMERICAL, Pattern.compile("[1-9][0-9]{0,4}")
            /* TODO UTF-8 */);

    /**
     * Split flag vector according to given flag type.
     *
     * @param flags    the flag vector
     * @param flagType the flag type
     * @return the flags as string
     */
    public static Collection<Flag> split(final String flags, FlagType flagType) {
        if (flags == null) {
            return Collections.emptyList();
        }

        final Pattern flagPattern = PATTERNS.get(flagType);
        if (flagPattern == null) {
            throw new UnsupportedOperationException("Flag type " + flagType + " is not supported");
        }

        final Matcher matcher = flagPattern.matcher(flags);
        final Collection<Flag> splitFlags = new ArrayList<>();
        while (matcher.find()) {
            splitFlags.add(new Flag(matcher.group()));
        }

        return splitFlags;
    }
}
