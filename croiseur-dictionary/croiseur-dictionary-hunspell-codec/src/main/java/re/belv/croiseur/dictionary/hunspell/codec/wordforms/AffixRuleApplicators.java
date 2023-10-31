/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.wordforms;

import re.belv.croiseur.dictionary.hunspell.codec.model.aff.AffixRule;

import java.util.Optional;

final class AffixRuleApplicators {

    /** Special affix value denoting absence of affix. */
    private static final String ZERO_AFFIX = "0";

    /** {@link AffixRuleApplicator} to use when affix is {@value ZERO_AFFIX}. */
    private static final AffixRuleApplicator ZERO_AFFIX_APPLICATOR = stem -> Optional.empty();

    /**
     * Private constructor, static methods only.
     */
    private AffixRuleApplicators() {
        // Nothing to do
    }

    /**
     * Build an {@link AffixRuleApplicator} based on {@link AffixRule}
     *
     * @param rule the parsed affix rule
     * @return the {@link AffixRuleApplicator}
     */
    static AffixRuleApplicator ofRule(final AffixRule rule) {

        final AffixRuleApplicator affixRuleApplicator;

        if (ZERO_AFFIX.equals(rule.affix())) {
            affixRuleApplicator = ZERO_AFFIX_APPLICATOR;
        } else if (rule.isPrefix()) {
            affixRuleApplicator = new PrefixRuleApplicator(rule);
        } else {
            affixRuleApplicator = new SuffixRuleApplicator(rule);
        }

        return affixRuleApplicator;
    }

}
