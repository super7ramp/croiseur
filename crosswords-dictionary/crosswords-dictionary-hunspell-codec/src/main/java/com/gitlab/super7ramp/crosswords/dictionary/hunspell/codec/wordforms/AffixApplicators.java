/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.wordforms;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.aff.AffixRule;

import java.util.Optional;

final class AffixApplicators {

    /** Special affix value denoting absence of affix. */
    private static final String ZERO_AFFIX = "0";

    /** {@link AffixApplicator} to used when affix is 0. */
    private static final AffixApplicator ZERO_AFFIX_APPLICATOR = stem -> Optional.empty();

    /**
     * Private constructor, static methods only.
     */
    private AffixApplicators() {
        // Nothing to do
    }

    /**
     * Build an {@link AffixApplicator} based on {@link AffixRule}
     *
     * @param rule the parsed affix rule
     * @return the {@link AffixApplicator}
     */
    static AffixApplicator ofRule(final AffixRule rule) {

        final AffixApplicator affixApplicator;

        if (ZERO_AFFIX.equals(rule.affix())) {
            affixApplicator = ZERO_AFFIX_APPLICATOR;
        } else if (rule.kind().isPrefix()) {
            affixApplicator = new PrefixApplicator(rule);
        } else {
            affixApplicator = new SuffixApplicator(rule);
        }

        return affixApplicator;
    }

}
