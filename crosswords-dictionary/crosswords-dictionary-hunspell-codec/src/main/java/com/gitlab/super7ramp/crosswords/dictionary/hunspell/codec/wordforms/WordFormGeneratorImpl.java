/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.wordforms;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.aff.Aff;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.aff.AffixRule;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.common.Flag;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.dic.Dic;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.dic.DicEntry;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.util.MoreCollections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

/**
 * Pure Java implementation of {@link WordFormGenerator}.
 * <p>
 * Note: This implements a minimal amount of Hunspell options.
 */
// FIXME vomit code to split and clean
final class WordFormGeneratorImpl implements WordFormGenerator {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(WordFormGeneratorImpl.class.getName());

    /** The parsed dictionary file. */
    private final Dic dic;

    /** Affix classes indexed by name to avoid looping the affix class list all the time. */
    private final AffixClasses affixClasses;

    /**
     * Affix rule applicators indexed by affix rule to avoid re-creating the same applicator over
     * and over.
     */
    private final Map<AffixRule, AffixRuleApplicator> affixApplicators;

    /** The compound flag, if any. */
    private final Optional<Flag> optCompoundFlag;

    private final AffixClassesApplicator affixClassesApplicator;

    /**
     * Constructor.
     *
     * @param anAff the parsed affix file
     * @param aDic  the parsed dictionary file
     */
    WordFormGeneratorImpl(final Aff anAff, final Dic aDic) {
        affixClasses = new AffixClasses(anAff);
        affixClassesApplicator = new AffixClassesApplicator(affixClasses);
        affixApplicators = new HashMap<>();
        optCompoundFlag = anAff.compoundFlag();
        dic = aDic;
    }

    @Override
    public Stream<String> generate() {
        final Stream<String> affixed = applyAffixes();

        final Stream<String> compounded;
        if (optCompoundFlag.isPresent()) {
            final Flag compoundFlag = optCompoundFlag.get();
            final Set<DicEntry> compoundable = dic.entries()
                                                  .stream()
                                                  .filter(entry -> entry.flags()
                                                                        .contains(compoundFlag))
                                                  .collect(toSet());
            compounded = MoreCollections.pairs(compoundable).stream().mapMulti((pair, consumer) -> {
                final String leftRightCompound = pair.left().word() + pair.right().word();
                final String rightLeftCompound = pair.right().word() + pair.left().word();
                consumer.accept(leftRightCompound);
                consumer.accept(rightLeftCompound);

                // Apply prefixes of left and suffixes of right on leftRightCompound
                prefixRulesApplicableTo(pair.left())
                        .peek(rule -> System.out.println("Applying " + rule + " on " + leftRightCompound))
                        .flatMap(rule -> applyAffix(rule, leftRightCompound).stream())
                        .flatMap(prefixedLeftRightCompound -> {
                            final Stream<String> crossProduct =
                                    suffixRulesApplicableTo(pair.right())
                                            .peek(rule -> System.out.println("Applying " + rule + " on " + prefixedLeftRightCompound))
                                            .flatMap(rule -> applyAffix(rule,
                                                    prefixedLeftRightCompound).stream());
                            return Stream.concat(Stream.of(prefixedLeftRightCompound),
                                    crossProduct);
                        })
                        .forEach(consumer);

                suffixRulesApplicableTo(pair.right())
                        .peek(rule -> System.out.println("Applying " + rule + " on " + leftRightCompound))
                        .flatMap(rule -> applyAffix(rule, leftRightCompound).stream())
                        .flatMap(suffixedLeftRightCompound -> {
                            final Stream<String> crossProduct =
                                    prefixRulesApplicableTo(pair.left())
                                            .peek(rule -> System.out.println("Applying " + rule + " on " + suffixedLeftRightCompound))
                                            .flatMap(rule -> applyAffix(rule,
                                                    suffixedLeftRightCompound).stream());
                            return Stream.concat(Stream.of(suffixedLeftRightCompound),
                                    crossProduct);
                        })
                        .forEach(consumer);

                // Apply prefixes of right and suffixes of left on rightLeftCompound
                prefixRulesApplicableTo(pair.right())
                        .flatMap(rule -> applyAffix(rule, rightLeftCompound).stream())
                        .flatMap(prefixedRightLeftCompound -> {
                            final Stream<String> crossProduct =
                                    suffixRulesApplicableTo(pair.left())
                                            .flatMap(rule -> applyAffix(rule,
                                                    prefixedRightLeftCompound).stream());
                            return Stream.concat(Stream.of(prefixedRightLeftCompound),
                                    crossProduct);
                        })
                        .forEach(consumer);

                suffixRulesApplicableTo(pair.left())
                        .flatMap(rule -> applyAffix(rule, rightLeftCompound).stream())
                        .flatMap(suffixedRightLeftCompound -> {
                            final Stream<String> crossProduct =
                                    prefixRulesApplicableTo(pair.right())
                                            .flatMap(rule -> applyAffix(rule,
                                                    suffixedRightLeftCompound).stream());
                            return Stream.concat(Stream.of(suffixedRightLeftCompound),
                                    crossProduct);
                        })
                        .forEach(consumer);
            });
        } else {
            compounded = Stream.empty();
        }

        return Stream.concat(affixed, compounded);
    }

    private Stream<String> applyAffixes() {
        return dic.entries()
                  .stream()
                  .flatMap((final DicEntry entry) -> {
                      final Stream<String> nonAffixedForm = Stream.of(entry.word());
                      final Stream<String> affixedForms = affixClassesApplicator.apply(entry);
                      return Stream.concat(nonAffixedForm, affixedForms);
                  });
    }

    private Optional<String> applyAffix(final AffixRule affixRule, final String baseWord) {
        return affixApplicators.computeIfAbsent(affixRule,
                                       rule -> AffixRuleApplicators.ofRule(affixRule))
                               .apply(baseWord);
    }

    private Stream<AffixRule> prefixRulesApplicableTo(final DicEntry dicEntry) {
        return affixRulesOf(dicEntry.flags()).filter(AffixRule::isPrefix);
    }

    private Stream<AffixRule> suffixRulesApplicableTo(final DicEntry dicEntry) {
        return affixRulesOf(dicEntry.flags()).filter(AffixRule::isSuffix);
    }

    private Stream<AffixRule> affixRulesOf(final Collection<Flag> flags) {
        return affixClasses.referencedBy(flags)
                           .flatMap(affixClass -> affixClass.rules().stream());
    }

}
