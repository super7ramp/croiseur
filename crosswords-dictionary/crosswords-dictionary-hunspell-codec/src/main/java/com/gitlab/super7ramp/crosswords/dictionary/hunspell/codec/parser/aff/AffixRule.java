package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.Flag;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a parsed affix rule.
 */
public record AffixRule(AffixKind kind, Flag flag, Optional<String> strippingCharacters,
                        String affix, Optional<String> continuationClasses,
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
     * Parses an affix rule.
     *
     * @param line line to parse
     * @return the {@link AffixRule}
     */
    static AffixRule valueOf(final String line) {
        final Matcher matcher = PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Not an affix rule: " + line);
        }

        final AffixKind kind = AffixKind.valueOf(matcher.group("kind"));
        final Flag flag = new Flag(matcher.group("flag"));
        final Optional<String> strippingCharacters = Optional.ofNullable(matcher.group(
                "strippingCharacters"));
        final String affix = matcher.group("affix");
        // FIXME should be a Collection<Flag> but we don't know flag type before hand
        final Optional<String> continuationClasses = Optional.ofNullable(matcher.group(
                "continuationClasses"));
        final Optional<String> condition = Optional.ofNullable(matcher.group("condition"));

        return new AffixRule(kind, flag, strippingCharacters, affix, continuationClasses,
                condition);
    }

}
