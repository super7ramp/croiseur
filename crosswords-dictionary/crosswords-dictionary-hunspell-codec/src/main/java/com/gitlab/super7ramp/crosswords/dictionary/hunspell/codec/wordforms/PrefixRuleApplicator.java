/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.wordforms;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.aff.AffixRule;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Implementation of {@link AffixRuleApplicator} for prefix.
 */
final class PrefixRuleApplicator implements AffixRuleApplicator {

    /** The rule data. */
    private final AffixRule rule;

    /** The condition pattern. */
    private final Optional<Pattern> conditionPattern;

    /** The stripping pattern. */
    private final Optional<Pattern> strippingPattern;

    /**
     * Constructs an instance.
     *
     * @param aRule the rule data
     */
    PrefixRuleApplicator(final AffixRule aRule) {
        rule = validate(aRule);
        conditionPattern = compileConditionPattern(aRule);
        strippingPattern = compileStrippingPattern(aRule);
    }

    /**
     * Builds the condition {@link Pattern} from the condition characters.
     *
     * @param aRule the prefix rule
     * @return the condition {@link Pattern}
     */
    private static Optional<Pattern> compileConditionPattern(final AffixRule aRule) {
        return aRule.condition().map(condition -> Pattern.compile("^" + condition));
    }

    /**
     * Builds the stripping {@link Pattern} from the stripping characters.
     *
     * @param aRule the prefix rule
     * @return the stripping {@link Pattern}
     */
    private static Optional<Pattern> compileStrippingPattern(final AffixRule aRule) {
        return aRule.strippingCharacters().map(stripped -> Pattern.compile("^" + stripped));
    }

    /**
     * Ensures given rule is a prefix rule.
     *
     * @param rule a rule
     * @return the given rule if it is a prefix rule
     * @throws IllegalArgumentException if given rule is not a prefix rule
     */
    private static AffixRule validate(final AffixRule rule) {
        if (!rule.isPrefix()) {
            throw new IllegalArgumentException(rule + " is not a prefix");
        }
        return rule;
    }

    @Override
    public Optional<String> apply(final String stem) {
        return isApplicable(stem) ? Optional.of(rule.affix() + strip(stem)) : Optional.empty();
    }

    private boolean isApplicable(final String stem) {
        return conditionPattern.map(p -> p.matcher(stem).find()).orElse(true);
    }

    private String strip(final String stem) {
        return strippingPattern.map(p -> p.matcher(stem).replaceFirst("")).orElse(stem);
    }
}
