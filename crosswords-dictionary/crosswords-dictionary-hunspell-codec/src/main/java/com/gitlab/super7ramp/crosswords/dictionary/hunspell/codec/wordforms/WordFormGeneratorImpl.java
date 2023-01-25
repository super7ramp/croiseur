/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.wordforms;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.aff.Aff;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.dic.Dic;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.dic.DicEntry;

import java.util.stream.Stream;

/**
 * Pure Java implementation of {@link WordFormGenerator}.
 * <p>
 * Note: This implements a minimal amount of Hunspell options.
 */
final class WordFormGeneratorImpl implements WordFormGenerator {

    /** The parsed dictionary file. */
    private final Dic dic;

    /** The function to create affixed forms. */
    private final Affixer affixer;

    /** The function to create (simple) compound forms. */
    private final Compounder simpleCompounder;

    /**
     * Constructor.
     *
     * @param affArg the parsed affix file
     * @param dicArg the parsed dictionary file
     */
    WordFormGeneratorImpl(final Aff affArg, final Dic dicArg) {
        dic = dicArg;
        final AffixClasses affixClasses = new AffixClasses(affArg);
        affixer = new Affixer(affixClasses);
        simpleCompounder = affArg.compoundFlag()
                                 .<Compounder>map(flag -> new SimpleCompounder(flag, affixer))
                                 .orElse(dicEntries -> Stream.empty());
    }

    @Override
    public Stream<String> generate() {
        final Stream<String> affixed = applyAffixes();
        final Stream<String> compounded = applyCompounds();
        return Stream.concat(affixed, compounded);
    }

    private Stream<String> applyCompounds() {
        return simpleCompounder.apply(dic.entries());
    }

    private Stream<String> applyAffixes() {
        return dic.entries()
                  .stream()
                  .flatMap((final DicEntry entry) -> {
                      final Stream<String> nonAffixedForm = Stream.of(entry.word());
                      final Stream<String> affixedForms = affixer.apply(entry);
                      return Stream.concat(nonAffixedForm, affixedForms);
                  });
    }

}
