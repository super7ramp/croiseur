/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec.parser.aff;

import com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec.model.aff.AffixClass;
import com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec.model.aff.AffixClassHeader;
import com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec.model.aff.AffixRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds an {@link AffixClass}.
 */
final class AffixClassBuilder {

    /** The affix rules. */
    private final List<AffixRule> rules;

    /** The affix header. */
    private AffixClassHeader header;

    /**
     * Constructor.
     */
    AffixClassBuilder() {
        rules = new ArrayList<>();
    }

    /**
     * Adds an {@link AffixClassHeader} to this builder.
     *
     * @param anHeader the header to add
     * @return this builder
     */
    AffixClassBuilder addHeader(final AffixClassHeader anHeader) {
        header = anHeader;
        return this;
    }

    /**
     * Adds an {@link AffixRule} to this {@link AffixClassBuilder}.
     *
     * @param rule the rule to add
     * @return this builder
     */
    AffixClassBuilder addRule(final AffixRule rule) {
        rules.add(rule);
        return this;
    }

    /**
     * Builds the affix.
     *
     * @return an {@link AffixClass}
     * @throws MissingAffixNameException if a semantic definition of the affix has problems
     * @throws InconsistentNumberOfRules if a semantic definition of the affix has problems
     */
    AffixClass build() throws MissingAffixNameException, InconsistentNumberOfRules {
        if (header == null) {
            throw new MissingAffixNameException();
        }

        if (header.numberOfRules() != rules.size()) {
            throw new InconsistentNumberOfRules();
        }

        return new AffixClass(header, rules);
    }
}
