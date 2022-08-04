package com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure.parser.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure.parser.common.Flag;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An affix header as parsed from an ".aff" file.
 */
public record AffixHeader(AffixKind kind, Flag flag, boolean crossProduct, int numberOfRules) {

    /** The pattern of an affix header. */
    private static final Pattern PATTERN = Pattern.compile("^(?<kind>(PFX|SFX)) +" +
            "(?<flag>[^ /]+) +" +
            "(?<crossProduct>[YN]) +" +
            "(?<numberOfRules>[0-9]+)$");

    /**
     * Parse an affix header line.
     *
     * @param line the line to parse
     * @return the {@link AffixHeader}
     */
    static AffixHeader valueOf(final String line) {
        final Matcher matcher = PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Not an affix header: " + line);
        }

        final AffixKind kind = AffixKind.valueOf(matcher.group("kind"));
        final Flag flag = new Flag(matcher.group("flag"));
        final boolean crossProduct = "Y".equals(matcher.group("crossProduct"));
        final int numberOfRules = Integer.parseInt(matcher.group("numberOfRules"));

        return new AffixHeader(kind, flag, crossProduct, numberOfRules);
    }
}
