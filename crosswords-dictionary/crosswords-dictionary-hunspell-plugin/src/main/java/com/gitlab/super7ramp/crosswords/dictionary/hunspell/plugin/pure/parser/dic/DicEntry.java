package com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.dic;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.common.Flag;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.common.FlagType;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parsed dictionary entry.
 */
public record DicEntry(boolean isForbidden, String word, Collection<Flag> flags) {

    /** The pattern of a dictionary entry. */
    private static final Pattern PATTERN = Pattern.compile("^(?<forbidden>\\*)?" +
            "(?<word>[^ /]+)" +
            "(/(?<flags>[^ /]+))?");

    /**
     * Parse a {@link DicEntry}.
     *
     * @param line the line to parse
     * @return a {@link DicEntry}
     * @throws InvalidDicEntryException if parsing goes wrong
     */
    static DicEntry valueOf(final String line) throws InvalidDicEntryException {
        return valueOf(line, FlagType.SINGLE_ASCII);
    }

    /**
     * Parse a {@link DicEntry}.
     *
     * @param line     the line to parse
     * @param flagType the flag type
     * @return a {@link DicEntry}
     * @throws InvalidDicEntryException if parsing goes wrong
     */
    static DicEntry valueOf(final String line, FlagType flagType) throws InvalidDicEntryException {

        final Matcher matcher = PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new InvalidDicEntryException("Not a dic entry: " + line);
        }

        final boolean isForbidden = matcher.group("forbidden") != null;
        final String word = matcher.group("word");
        final Collection<Flag> flags = Flag.split(matcher.group("flags"), flagType);

        return new DicEntry(isForbidden, word, flags);
    }

}