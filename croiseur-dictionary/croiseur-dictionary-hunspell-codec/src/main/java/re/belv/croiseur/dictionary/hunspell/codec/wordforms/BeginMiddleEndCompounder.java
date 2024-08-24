/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.wordforms;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import re.belv.croiseur.dictionary.hunspell.codec.model.aff.ThreePartsCompoundFlags;
import re.belv.croiseur.dictionary.hunspell.codec.model.common.Flag;
import re.belv.croiseur.dictionary.hunspell.codec.model.dic.DicEntry;
import re.belv.croiseur.dictionary.hunspell.codec.util.MoreCollections;
import re.belv.croiseur.dictionary.hunspell.codec.util.Triplet;

/**
 * Creates compounds based on the compound begin/middle/end flag options.
 *
 * <p>Every triplet of entries (word1,word2,word3) referencing respectively the compound begin flag, compound middle
 * flag and compound end flags can be compounded as "word1word2word3".
 */
final class BeginMiddleEndCompounder implements Compounder {

    /** The begin compound flags. */
    private final Flag beginFlag;

    /** The middle compound flags. */
    private final Flag middleFlag;

    /** The end compound flags. */
    private final Flag endFlag;

    /** The function to create affix form. */
    private final Affixer affixer;

    /**
     * Constructs an instance.
     *
     * @param flags the compound flags
     * @param affixerArg the affixer
     */
    BeginMiddleEndCompounder(ThreePartsCompoundFlags flags, final Affixer affixerArg) {
        beginFlag = flags.begin();
        middleFlag = flags.middle();
        endFlag = flags.end();
        affixer = affixerArg;
    }

    @Override
    public Stream<String> apply(final Collection<DicEntry> dicEntries) {

        final Map<Flag, Set<DicEntry>> compoundableEntries = groupCompoundableEntries(dicEntries);

        final Set<DicEntry> beginnings = compoundableEntries.get(beginFlag);
        final Set<DicEntry> middles = compoundableEntries.get(middleFlag);
        final Set<DicEntry> ends = compoundableEntries.get(endFlag);

        return MoreCollections.triplets(beginnings, middles, ends).stream().mapMulti((compoundParts, accumulator) -> {
            final BeginMiddleEndCompound compound = compound(compoundParts, accumulator);
            applyAffixes(compound, accumulator);
        });
    }

    private Map<Flag, Set<DicEntry>> groupCompoundableEntries(final Collection<DicEntry> dicEntries) {
        final Map<Flag, Set<DicEntry>> compoundableParts =
                Map.of(beginFlag, new HashSet<>(), middleFlag, new HashSet<>(), endFlag, new HashSet<>());
        for (final DicEntry dicEntry : dicEntries) {
            if (dicEntry.isFlaggedWith(beginFlag)) {
                compoundableParts.get(beginFlag).add(dicEntry);
            }
            if (dicEntry.isFlaggedWith(middleFlag)) {
                compoundableParts.get(middleFlag).add(dicEntry);
            }
            if (dicEntry.isFlaggedWith(endFlag)) {
                compoundableParts.get(endFlag).add(dicEntry);
            }
        }
        return compoundableParts;
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
    private BeginMiddleEndCompound compound(
            final Triplet<DicEntry, DicEntry, DicEntry> compoundParts, final Consumer<String> accumulator) {
        final BeginMiddleEndCompound compound =
                new BeginMiddleEndCompound(compoundParts.left(), compoundParts.middle(), compoundParts.right());
        accumulator.accept(compound.word());
        return compound;
    }

    /**
     * Applies the affixes on the given compound.
     *
     * @param compound the compound
     * @param accumulator the accumulator where the compound is added.
     */
    private void applyAffixes(final BeginMiddleEndCompound compound, final Consumer<String> accumulator) {
        affixer.apply(compound).forEach(accumulator);
    }
}
