/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.wordforms;

import static java.util.stream.Collectors.toSet;
import static re.belv.croiseur.dictionary.hunspell.codec.util.MoreCollections.pairs;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import re.belv.croiseur.dictionary.hunspell.codec.model.common.Flag;
import re.belv.croiseur.dictionary.hunspell.codec.model.dic.DicEntry;
import re.belv.croiseur.dictionary.hunspell.codec.util.Pair;

/**
 * Creates compounds based on the compound flag option.
 *
 * <p>Every entry referencing the compound flag can be compounded with each other, in any order.
 */
final class BeginEndCompounder implements Compounder {

    /** The compound flag. */
    private final Flag compoundFlag;

    /** The function to create affix forms. */
    private final Affixer affixer;

    /**
     * Creates an instance.
     *
     * @param compoundFlagArg the compound flag
     * @param affixerArg the affixer
     */
    BeginEndCompounder(final Flag compoundFlagArg, final Affixer affixerArg) {
        compoundFlag = compoundFlagArg;
        affixer = affixerArg;
    }

    @Override
    public Stream<String> apply(final Collection<DicEntry> entries) {
        final Set<DicEntry> compoundableEntries = entries.stream()
                .filter(entry -> entry.isFlaggedWith(compoundFlag))
                .collect(toSet());

        return pairs(compoundableEntries).stream().mapMulti((compoundParts, accumulator) -> {
            final BeginEndCompound compound = compound(compoundParts, accumulator);
            applyAffixes(compound, accumulator);
        });
    }

    /**
     * Creates the compound from the given parts.
     *
     * <p>The created compounded is added to the accumulator.
     *
     * @param compoundParts the compound parts
     * @param accumulator the accumulator where the compound is added
     * @return the created compound
     */
    private BeginEndCompound compound(
            final Pair<DicEntry, DicEntry> compoundParts, final Consumer<String> accumulator) {
        final BeginEndCompound compound = new BeginEndCompound(compoundParts.left(), compoundParts.right());
        accumulator.accept(compound.word());
        return compound;
    }

    /**
     * Applies the affixes on the given compound.
     *
     * @param compound the compound
     * @param accumulator the accumulator where the compound is added.
     */
    private void applyAffixes(final BeginEndCompound compound, final Consumer<String> accumulator) {
        affixer.apply(compound).forEach(accumulator);
    }
}
