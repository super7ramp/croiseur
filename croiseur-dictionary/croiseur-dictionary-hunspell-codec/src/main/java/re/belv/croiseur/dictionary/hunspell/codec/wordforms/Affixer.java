/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.wordforms;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import re.belv.croiseur.dictionary.hunspell.codec.model.aff.AffixClass;
import re.belv.croiseur.dictionary.hunspell.codec.model.common.Flag;
import re.belv.croiseur.dictionary.hunspell.codec.model.dic.DicEntry;

/** Applies all the appropriate affix classes to a dic entry. */
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
            affixClassApplicators.put(affixClass.flag(), new AffixClassApplicator(affixClass, affixClasses));
        }
    }

    @Override
    public Stream<String> apply(final DicEntry entry) {
        return affixClasses
                .referencedBy(entry.flags())
                .map(cls -> affixClassApplicators.get(cls.flag()))
                .flatMap(affixClassApplicator -> affixClassApplicator.apply(entry));
    }

    /**
     * Applies affix classes on a compound.
     *
     * <p>It is slightly more complex than on a normal entry:
     *
     * <ul>
     *   <li>Apply prefixes referenced by the begin part on the compound
     *   <li>Apply suffixes referenced by the end part on the prefixed compound (cross-product)
     *   <li>Apply suffixes referenced by the end part on the compound
     *   <li>Apply prefixes referenced by begin part on the suffixed compound (cross-product)
     * </ul>
     *
     * @param compound the compound to affix
     * @return the affixed forms
     */
    // TODO Affixes with COMPOUNDPERMITFLAG may be inside of compounds
    Stream<String> apply(final BeginEndCompound compound) {

        final String compoundedForm = compound.word();
        final DicEntry prefixablePseudoDicEntry =
                new DicEntry(false, compoundedForm, compound.endFlags() /* for cross-product
                suffix on prefixed form. */);
        final Stream<String> prefixed = affixClasses
                .referencedBy(compound.beginFlags())
                .filter(cls -> cls.kind().isPrefix())
                .map(cls -> affixClassApplicators.get(cls.flag()))
                .flatMap(affixClassApplicator -> affixClassApplicator.apply(prefixablePseudoDicEntry));

        final DicEntry suffixablePseudoDicEntry =
                new DicEntry(false, compoundedForm, compound.beginFlags() /* for cross-product
                prefix on suffixed form*/);
        final Stream<String> suffixed = affixClasses
                .referencedBy(compound.endFlags())
                .filter(cls -> cls.kind().isSuffix())
                .map(cls -> affixClassApplicators.get(cls.flag()))
                .flatMap(affixClassApplicator -> affixClassApplicator.apply(suffixablePseudoDicEntry));

        return Stream.concat(prefixed, suffixed);
    }

    /**
     * Applies affix classes on a begin-middle-end compound.
     *
     * @param compound the compound to affix
     * @return the affixed forms
     */
    // TODO Affixes with COMPOUNDPERMITFLAG may be inside of compounds
    Stream<String> apply(final BeginMiddleEndCompound compound) {

        final String compoundedForm = compound.word();
        final DicEntry prefixablePseudoDicEntry =
                new DicEntry(false, compoundedForm, compound.endFlags() /* for cross-product
                suffix on prefixed form. */);
        final Stream<String> prefixed = affixClasses
                .referencedBy(compound.beginFlags())
                .filter(cls -> cls.kind().isPrefix())
                .map(cls -> affixClassApplicators.get(cls.flag()))
                .flatMap(affixClassApplicator -> affixClassApplicator.apply(prefixablePseudoDicEntry));

        final DicEntry suffixablePseudoDicEntry =
                new DicEntry(false, compoundedForm, compound.beginFlags() /* for cross-product
                prefix on suffixed form*/);
        final Stream<String> suffixed = affixClasses
                .referencedBy(compound.endFlags())
                .filter(cls -> cls.kind().isSuffix())
                .map(cls -> affixClassApplicators.get(cls.flag()))
                .flatMap(affixClassApplicator -> affixClassApplicator.apply(suffixablePseudoDicEntry));

        return Stream.concat(prefixed, suffixed);
    }
}
