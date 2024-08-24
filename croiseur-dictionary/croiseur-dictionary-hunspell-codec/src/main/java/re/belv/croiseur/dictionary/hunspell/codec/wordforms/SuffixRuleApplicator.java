/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.wordforms;

import java.util.Optional;
import java.util.regex.Pattern;
import re.belv.croiseur.dictionary.hunspell.codec.model.aff.AffixRule;

/** Implementation of {@link AffixRuleApplicator} for prefix. */
final class SuffixRuleApplicator implements AffixRuleApplicator {

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
    SuffixRuleApplicator(final AffixRule aRule) {
        rule = validate(aRule);
        conditionPattern = compileConditionPattern(aRule);
        strippingPattern = compileStrippingPattern(aRule);
    }

    /**
     * Builds the condition {@link Pattern} from the condition characters.
     *
     * @param rule the suffix rule
     * @return the condition {@link Pattern}
     */
    private static Optional<Pattern> compileConditionPattern(final AffixRule rule) {
        return rule.condition().map(condition -> Pattern.compile(condition + "$"));
    }

    /**
     * Builds the stripping {@link Pattern} from the stripping characters.
     *
     * @param rule the suffix rule
     * @return the stripping {@link Pattern}
     */
    private static Optional<Pattern> compileStrippingPattern(final AffixRule rule) {
        return rule.strippingCharacters().map(stripped -> Pattern.compile(stripped + "$"));
    }

    /**
     * Ensures the given rule is a suffix rule.
     *
     * @param rule a rule
     * @return the given rule if it is a suffix rule
     * @throws IllegalArgumentException if given rule is not a suffix rule
     */
    private static AffixRule validate(final AffixRule rule) {
        if (!rule.isSuffix()) {
            throw new IllegalArgumentException(rule + " is not a suffix");
        }
        return rule;
    }

    @Override
    public Optional<String> apply(final String stem) {
        return isApplicable(stem) ? Optional.of(strip(stem) + rule.affix()) : Optional.empty();
    }

    private boolean isApplicable(final String stem) {
        return conditionPattern.map(p -> p.matcher(stem).find()).orElse(true);
    }

    private String strip(final String stem) {
        return strippingPattern.map(p -> p.matcher(stem).replaceFirst("")).orElse(stem);
    }
}
