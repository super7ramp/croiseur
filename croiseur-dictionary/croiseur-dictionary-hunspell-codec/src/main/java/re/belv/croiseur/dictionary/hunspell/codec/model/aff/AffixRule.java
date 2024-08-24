/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.model.aff;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import re.belv.croiseur.dictionary.hunspell.codec.model.common.Flag;

/**
 * Represents a parsed affix rule.
 *
 * @param kind                the affix rule kind
 * @param flag                the flag which applies this rule
 * @param strippingCharacters characters to strip from base word before applying the affix
 * @param affix               the affix to apply
 * @param continuationClasses the affix classes that can be applied on the affixed word
 * @param condition           pattern that the base word must match to be eligible to the affix rule
 */
public record AffixRule(
        AffixKind kind,
        Flag flag,
        Optional<String> strippingCharacters,
        String affix,
        Collection<Flag> continuationClasses,
        Optional<String> condition) {

    /**
     * Performs some null checks.
     *
     * @param kind                the affix rule kind
     * @param flag                the flag which applies this rule
     * @param strippingCharacters characters to strip from base word before applying the affix
     * @param affix               the affix to apply
     * @param continuationClasses the affix classes that can be applied on the affixed word
     * @param condition           pattern that the base word must match to be eligible to the
     *                            affix rule
     */
    public AffixRule {
        Objects.requireNonNull(kind);
        Objects.requireNonNull(flag);
        Objects.requireNonNull(strippingCharacters);
        Objects.requireNonNull(affix);
        Objects.requireNonNull(continuationClasses);
        Objects.requireNonNull(condition);
    }

    /**
     * Returns whether this rule is a suffix rule.
     *
     * @return {@code true} iff this rule is a suffix rule
     */
    public boolean isSuffix() {
        return kind.isSuffix();
    }

    /**
     * Returns whether this rule is a prefix rule.
     *
     * @return {@code true} iff this rule is a prefix rule
     */
    public boolean isPrefix() {
        return kind.isPrefix();
    }
}
