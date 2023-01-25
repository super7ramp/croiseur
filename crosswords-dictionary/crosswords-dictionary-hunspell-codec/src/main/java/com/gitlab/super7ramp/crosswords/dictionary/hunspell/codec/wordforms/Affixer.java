/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.wordforms;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.aff.AffixClass;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.common.Flag;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.dic.DicEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Applies all the appropriate affix classes to a dic entry.
 */
final class Affixer implements Function<DicEntry, Stream<String>> {

    /** The affix classes access. */
    private final AffixClasses affixClasses;

    /** The cache of applicators. */
    private final Map<Flag, AffixClassApplicator> affixClassApplicators;

    /**
     * Constructs an instance.
     *
     * @param affixClassesArg access to all the affix classes
     */
    Affixer(final AffixClasses affixClassesArg) {
        affixClasses = affixClassesArg;
        affixClassApplicators = new HashMap<>();
        for (final AffixClass affixClass : affixClasses) {
            affixClassApplicators.put(affixClass.flag(), new AffixClassApplicator(affixClass,
                    affixClasses));
        }
    }

    @Override
    public Stream<String> apply(final DicEntry entry) {
        return affixClasses.referencedBy(entry.flags())
                           .map(cls -> affixClassApplicators.get(cls.flag()))
                           .flatMap(affixClassApplicator -> affixClassApplicator.apply(entry));
    }

    /**
     * Applies affix classes on a compound.
     * <p>
     * It is slightly more complex than on a normal entry:
     * <ul>
     *     <li>Apply prefixes referenced by the left part on the compound</li>
     *     <li>Apply suffixes referenced by the right part on the prefixed compound
     *     (cross-product)</li>
     *     <li>Apply suffixes referenced by the right part on the compound</li>
     *     <li>Apply prefixes referenced by left part on the suffixed compound (cross-product)</li>
     * </ul>
     *
     * @param compound the compound to affix
     * @return the affixed forms
     */
    Stream<String> apply(final SimpleCompound compound) {

        final String compoundedForm = compound.word();
        final DicEntry prefixablePseudoDicEntry =
                new DicEntry(false, compoundedForm, compound.rightFlags() /* for cross-product
                suffix on prefixed form. */);
        final Stream<String> prefixed =
                affixClasses.referencedBy(compound.leftFlags())
                            .filter(cls -> cls.kind().isPrefix())
                            .map(cls -> affixClassApplicators.get(cls.flag()))
                            .flatMap(affixClassApplicator -> affixClassApplicator.apply(prefixablePseudoDicEntry));

        final DicEntry suffixablePseudoDicEntry =
                new DicEntry(false, compoundedForm, compound.leftFlags() /* for cross-product
                prefix on suffixed form*/);
        final Stream<String> suffixed =
                affixClasses.referencedBy(compound.rightFlags())
                            .filter(cls -> cls.kind().isSuffix())
                            .map(cls -> affixClassApplicators.get(cls.flag()))
                            .flatMap(affixClassApplicator -> affixClassApplicator.apply(suffixablePseudoDicEntry));

        return Stream.concat(prefixed, suffixed);
    }
}

