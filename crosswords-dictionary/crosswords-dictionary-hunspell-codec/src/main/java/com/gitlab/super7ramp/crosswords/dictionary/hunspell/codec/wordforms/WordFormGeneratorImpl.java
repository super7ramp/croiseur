/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.wordforms;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff.Aff;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff.AffixClass;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff.AffixRule;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.Flag;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.dic.Dic;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.dic.DicEntry;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.util.MoreCollections;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

/**
 * Pure Java implementation of {@link WordFormGenerator}.
 * <p>
 * Note: This implements a minimal amount of Hunspell options.
 */
final class WordFormGeneratorImpl implements WordFormGenerator {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(WordFormGeneratorImpl.class.getName());

    /** The parsed dictionary file. */
    private final Dic dic;

    /** Affix classes indexed by name to avoid looping the affix class list all the time. */
    private final Map<Flag, AffixClass> affixClasses;

    /**
     * Affix rule applicators indexed by affix rule to avoid re-creating the same applicator over
     * and over.
     */
    private final Map<AffixRule, AffixApplicator> affixApplicators;

    /** The compound flag, if any. */
    private final Optional<Flag> optCompoundFlag;

    /**
     * Constructor.
     *
     * @param anAff the parsed affix file
     * @param aDic  the parsed dictionary file
     */
    WordFormGeneratorImpl(final Aff anAff, final Dic aDic) {
        affixClasses = new HashMap<>();
        for (final AffixClass affixClass : anAff.affixClasses()) {
            affixClasses.put(affixClass.header().flag(), affixClass);
        }
        affixApplicators = new HashMap<>();
        optCompoundFlag = anAff.compoundFlag();
        dic = aDic;
    }

    @Override
    public Stream<String> generate() {
        final Stream<String> affixed = dic.entries()
                                          .stream()
                                          .mapMulti(applyAffixes().andThen((entry, consumer) -> consumer.accept(entry.word())));

        final Stream<String> compounded;
        if (optCompoundFlag.isPresent()) {
            final Flag compoundFlag = optCompoundFlag.get();
            final Set<DicEntry> compoundable =
                    dic.entries()
                       .stream()
                       .filter(entry -> entry.flags().contains(compoundFlag))
                       .collect(toSet());
            compounded =
                    MoreCollections.pairs(compoundable).stream().mapMulti((pair, consumer) -> {
                        consumer.accept(pair.left().word() + pair.right().word());
                        consumer.accept(pair.right().word() + pair.left().word());
                    });
        } else {
            compounded = Stream.empty();
        }

        return Stream.concat(affixed, compounded);
    }

    private BiConsumer<DicEntry, Consumer<String>> applyAffixes() {
        return this::applyAffixes;
    }

    // TODO simplify/split
    private void applyAffixes(final DicEntry entry, final Consumer<String> accumulator) {
        for (final Flag flag : entry.flags()) {
            final AffixClass affixClass = affixClasses.get(flag);
            if (affixClass != null) {
                applyAffixRules(entry, accumulator, affixClass);
            }
        }
    }

    private void applyAffixRules(final DicEntry entry, final Consumer<String> accumulator,
                                 final AffixClass affixClass) {
        for (final AffixRule affixRule : affixClass.rules()) {

            final Optional<String> optAffixedForm = applyAffix(affixRule, entry.word());

            if (optAffixedForm.isPresent()) {
                final String affixedForm = optAffixedForm.get();
                accumulator.accept(affixedForm);

                if (affixClass.header().crossProduct()) {
                    entry.flags()
                         .stream()
                         .map(affixClasses::get)
                         .filter(Objects::nonNull)  // flag may refer to another option than PFX/SFX
                         .flatMap(crossProductClass -> crossProductClass.rules().stream())
                         .filter(rule -> rule.kind() != affixRule.kind())
                         .flatMap(rule -> applyAffix(rule, affixedForm).stream())
                         .limit(2)
                         .forEach(accumulator);
                }

                affixRule.continuationClasses()
                         .stream()
                         .map(affixClasses::get)
                         .filter(Objects::nonNull)  // flag may refer to another option than PFX/SFX
                         .flatMap(continuationClass -> continuationClass.rules().stream())
                         .flatMap(rule -> applyAffix(rule, affixedForm).stream())
                         .forEach(accumulator);
            }
        }
    }

    private Optional<String> applyAffix(final AffixRule affixRule, final String baseWord) {
        return affixApplicators.computeIfAbsent(affixRule,
                                       rule -> AffixApplicators.ofRule(affixRule))
                               .apply(baseWord);
    }

}
