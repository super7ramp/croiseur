package com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.wordforms;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.aff.AffixRule;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Implementation of {@link AffixApplicator} for prefix.
 */
final class SuffixApplicator implements AffixApplicator {

    /** The rule data. */
    private final AffixRule rule;

    /** The condition pattern. */
    private final Optional<Pattern> conditionPattern;

    /** The stripping pattern. */
    private final Optional<Pattern> strippingPattern;

    /**
     * Constructor.
     *
     * @param aRule the rule data
     */
    SuffixApplicator(final AffixRule aRule) {
        rule = validate(aRule);
        conditionPattern = compileConditionPattern(aRule);
        strippingPattern = compileStrippingPattern(aRule);
    }

    /**
     * Build the condition {@link Pattern} from the condition characters.
     *
     * @param aRule the prefix rule
     * @return the condition {@link Pattern}
     */
    private static Optional<Pattern> compileConditionPattern(final AffixRule aRule) {
        return aRule.condition().map(condition -> Pattern.compile(condition + "$"));
    }

    /**
     * Build the stripping {@link Pattern} from the stripping characters.
     *
     * @param aRule the prefix rule
     * @return the stripping {@link Pattern}
     */
    private static Optional<Pattern> compileStrippingPattern(final AffixRule aRule) {
        return aRule.strippingCharacters().map(stripped -> Pattern.compile(stripped + "$"));
    }

    /**
     * Ensure given rule is a suffix rule.
     *
     * @param aRule a rule
     * @return the given rule if it is a suffix rule
     * @throws IllegalArgumentException if given rule is not a suffix rule
     */
    private static AffixRule validate(final AffixRule aRule) {
        if (!aRule.kind().isSuffix()) {
            throw new IllegalArgumentException(aRule + " is not a suffix");
        }
        return aRule;
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
