/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.parser.aff;

import re.belv.croiseur.dictionary.hunspell.codec.model.aff.AffixKind;
import re.belv.croiseur.dictionary.hunspell.codec.model.aff.AffixRule;
import re.belv.croiseur.dictionary.hunspell.codec.model.common.Flag;
import re.belv.croiseur.dictionary.hunspell.codec.parser.common.FlagType;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses {@link AffixRule}.
 */
final class AffixRuleParser {

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
    static AffixRule parse(final String line) {
        return parse(line, FlagType.byDefault());
    }

    /**
     * Parses an affix rule.
     *
     * @param line     line to parse
     * @param flagType the flag type (in order to split the continuation classes)
     * @return the {@link AffixRule}
     */
    static AffixRule parse(final String line, final FlagType flagType) {
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
                       .map(flagType::split)
                       .orElseGet(Collections::emptyList);
    }
}
