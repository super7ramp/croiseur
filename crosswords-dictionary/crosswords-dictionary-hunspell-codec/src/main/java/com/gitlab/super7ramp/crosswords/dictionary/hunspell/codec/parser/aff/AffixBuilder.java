package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds an {@link Affix}.
 */
final class AffixBuilder {

    /** The affix rules. */
    private final List<AffixRule> rules;

    /** The affix header. */
    private AffixHeader header;

    /**
     * Constructor.
     */
    AffixBuilder() {
        rules = new ArrayList<>();
    }

    /**
     * Adds an {@link AffixHeader} to this builder.
     *
     * @param anHeader the header to add
     * @return this builder
     */
    AffixBuilder addHeader(final AffixHeader anHeader) {
        header = anHeader;
        return this;
    }

    /**
     * Adds an {@link AffixRule} to this {@link AffixBuilder}.
     *
     * @param rule the rule to add
     * @return this builder
     */
    AffixBuilder addRule(final AffixRule rule) {
        rules.add(rule);
        return this;
    }

    /**
     * Builds the affix.
     *
     * @return an {@link Affix}
     * @throws MissingAffixNameException if a semantic definition of the affix has problems
     * @throws InconsistentNumberOfRules if a semantic definition of the affix has problems
     */
    Affix build() throws MissingAffixNameException, InconsistentNumberOfRules {
        if (header == null) {
            throw new MissingAffixNameException();
        }

        if (header.numberOfRules() != rules.size()) {
            throw new InconsistentNumberOfRules();
        }

        return new Affix(header, rules);
    }
}
