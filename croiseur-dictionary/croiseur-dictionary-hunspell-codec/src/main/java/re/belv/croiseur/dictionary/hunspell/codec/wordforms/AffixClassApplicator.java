/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.wordforms;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import re.belv.croiseur.dictionary.hunspell.codec.model.aff.AffixClass;
import re.belv.croiseur.dictionary.hunspell.codec.model.aff.AffixRule;
import re.belv.croiseur.dictionary.hunspell.codec.model.common.Flag;
import re.belv.croiseur.dictionary.hunspell.codec.model.dic.DicEntry;

/**
 * Applies an affix class to a word.
 */
final class AffixClassApplicator implements Function<DicEntry, Stream<String>> {

    /** The affix class. */
    private final AffixClass affixClass;

    /** Access to the other affix classes (for continuation). */
    private final AffixClasses affixClasses;

    /** Affix applicators indexed by affix rule, lazily populated. */
    private final Map<AffixRule, AffixRuleApplicator> affixApplicators;

    /**
     * Constructs an instance.
     *
     * @param affixClassArg   the affix class
     * @param affixClassesArg the other affix classes
     */
    AffixClassApplicator(final AffixClass affixClassArg, final AffixClasses affixClassesArg) {
        affixClass = affixClassArg;
        affixClasses = affixClassesArg;
        affixApplicators = new HashMap<>();
    }

    @Override
    public Stream<String> apply(final DicEntry entry) {
        return affixClass.rules().stream().mapMulti((rule, accumulator) -> applyRule(rule, entry, accumulator));
    }

    /**
     * Applies the given rule to given stem, accumulating the generated strings in the given
     * accumulator.
     *
     * @param affixRule   the rule to apply
     * @param entry       the dictionary entry
     * @param accumulator the accumulator where to add new affixed forms
     */
    private void applyRule(final AffixRule affixRule, final DicEntry entry, final Consumer<String> accumulator) {

        final Optional<String> optAffixedForm = applyAffixRule(affixRule, entry.word());

        if (optAffixedForm.isPresent()) {
            final String affixedForm = optAffixedForm.get();
            accumulator.accept(affixedForm);
            applyCrossProductAffixRules(entry, affixedForm, accumulator);
            applyContinuationAffixRules(affixRule, affixedForm, accumulator);
        } else {
            // Rule not applicable, no affixed form
        }
    }

    /**
     * Applies the affix rules of the continuation classes of the given rule applied on the given
     * word.
     *
     * @param affixRule   the affix rule to continue
     * @param affixedForm the word affixed using the given affix rule
     * @param accumulator the accumulator where to add new affixed forms
     */
    private void applyContinuationAffixRules(
            final AffixRule affixRule, final String affixedForm, final Consumer<String> accumulator) {
        affixRulesOf(affixRule.continuationClasses())
                .flatMap(rule -> applyAffixRule(rule, affixedForm).stream())
                .forEach(accumulator);
    }

    /**
     * If cross-product is enabled for this class, applies, on the given already affixed form, the
     * rules:
     * <ul>
     *     <li>Referenced by the given entry;
     *     <li>For which cross-product is enabled;
     *     <li>Of opposite kind (i.e. suffix if this class is prefix, and vice-versa)</li>
     * </ul>
     *
     * <p>
     * Example:
     * <ul>
     *     <li>Rule: Suffix 'suf'</li>
     *     <li>Word: 'foosuf' (affixed form, stem was 'foo')</li>
     *     <li>Other classes rules: Prefix 'pre', prefix 'Pre'</li>
     *     <li>Cross-product result: 'prefoosuf' and 'Prefoosuf'.
     * </ul>
     *
     * @param affixedForm the word affixed using the given affix rule
     * @param accumulator the accumulator where to add new affixed forms
     */
    private void applyCrossProductAffixRules(
            final DicEntry entry, final String affixedForm, final Consumer<String> accumulator) {
        crossProductAffixRulesOf(entry)
                .flatMap(rule -> applyAffixRule(rule, affixedForm).stream())
                .forEach(accumulator);
    }

    /**
     * Gets the {@link AffixRuleApplicator} for the given rule.
     * <p>
     * Creates it if it is not cached yet.
     *
     * @param affixRule the affix rule
     * @return the corresponding {@link AffixRuleApplicator}
     */
    private AffixRuleApplicator applicatorOf(final AffixRule affixRule) {
        return affixApplicators.computeIfAbsent(affixRule, rule -> AffixRuleApplicators.ofRule(affixRule));
    }

    /**
     * Applies the given affix rule to the given word.
     *
     * @param affixRule the affix rule to apply
     * @param word      the word to affix
     * @return the affixed word, if rule is applicable
     */
    private Optional<String> applyAffixRule(final AffixRule affixRule, final String word) {
        return applicatorOf(affixRule).apply(word);
    }

    /**
     * Helper to get the affix rules of the affix classes identified by the given flags.
     *
     * @param flags the flags of the affix classes to get the affix rules from
     * @return the affix rules of the affix classes identified by the given flags
     */
    private Stream<AffixRule> affixRulesOf(final Collection<Flag> flags) {
        return affixClasses.referencedBy(flags).flatMap(cls -> cls.rules().stream());
    }

    /**
     * Helper to get the cross-product rules of this affix class for the given entry.
     *
     * @return the cross-product rules of this affix class for the given entry
     */
    private Stream<AffixRule> crossProductAffixRulesOf(final DicEntry entry) {
        final Stream<AffixRule> crossProductAffixRules;
        if (affixClass.crossProduct()) {
            crossProductAffixRules = affixClasses
                    .referencedBy(entry.flags())
                    .filter(cls -> cls.crossProduct() && cls.kind() != affixClass.kind())
                    .flatMap(cls -> cls.rules().stream());
        } else {
            crossProductAffixRules = Stream.empty();
        }
        return crossProductAffixRules;
    }
}
