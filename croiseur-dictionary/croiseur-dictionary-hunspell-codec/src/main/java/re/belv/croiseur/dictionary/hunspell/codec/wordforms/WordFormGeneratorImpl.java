/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.wordforms;

import re.belv.croiseur.dictionary.hunspell.codec.model.aff.Aff;
import re.belv.croiseur.dictionary.hunspell.codec.model.dic.Dic;
import re.belv.croiseur.dictionary.hunspell.codec.model.dic.DicEntry;

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

    /** The function to create begin-end compounds, based on the {@code COMPOUNDFLAG} option. */
    private final Compounder beginEndCompounder;

    /**
     * The function to create begin-middle-end compounds, based on the {@code COMPOUNDBEGIN},
     * {@code COMPOUNDMIDDLE} and {@code COMPOUNDEND} options.
     */
    private final Compounder beginMiddleEndCompounder;

    /**
     * Constructs an instance.
     *
     * @param affArg the parsed affix file
     * @param dicArg the parsed dictionary file
     */
    WordFormGeneratorImpl(final Aff affArg, final Dic dicArg) {
        dic = dicArg;
        affixer = new Affixer(new AffixClasses(affArg));
        beginEndCompounder =
                affArg.compoundFlag()
                      .<Compounder>map(flag -> new BeginEndCompounder(flag, affixer))
                      .orElse(dicEntries -> Stream.empty());
        beginMiddleEndCompounder =
                affArg.threePartsCompoundFlags()
                      .<Compounder>map(compoundBeginMiddleEndFlags -> new BeginMiddleEndCompounder(compoundBeginMiddleEndFlags, affixer))
                      .orElse(dicEntries -> Stream.empty());
    }

    @Override
    public Stream<String> generate() {
        final Stream<String> affixed = applyAffixes();
        final Stream<String> compounded = applyCompounds();
        return Stream.concat(affixed, compounded);
    }

    private Stream<String> applyCompounds() {
        final Stream<String> beginEndCompounds = beginEndCompounder.apply(dic.entries());
        final Stream<String> beginMiddleEndCompounds =
                beginMiddleEndCompounder.apply(dic.entries());
        return Stream.concat(beginEndCompounds, beginMiddleEndCompounds);
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
