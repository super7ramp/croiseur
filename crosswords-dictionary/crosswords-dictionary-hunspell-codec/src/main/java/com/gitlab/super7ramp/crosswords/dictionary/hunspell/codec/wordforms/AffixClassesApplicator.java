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
final class AffixClassesApplicator implements Function<DicEntry, Stream<String>> {

    /** The affix classes access. */
    private final AffixClasses affixClasses;

    /** The cache of applicators. */
    private final Map<Flag, AffixClassApplicator> affixClassApplicators;

    /**
     * Constructs an instance.
     *
     * @param affixClassesArg access to all the affix classes
     */
    AffixClassesApplicator(final AffixClasses affixClassesArg) {
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

}
