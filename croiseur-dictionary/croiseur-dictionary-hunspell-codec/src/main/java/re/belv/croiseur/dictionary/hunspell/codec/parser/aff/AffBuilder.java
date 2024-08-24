/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.parser.aff;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import re.belv.croiseur.dictionary.hunspell.codec.model.aff.Aff;
import re.belv.croiseur.dictionary.hunspell.codec.model.aff.AffixClass;
import re.belv.croiseur.dictionary.hunspell.codec.model.aff.AffixClassHeader;
import re.belv.croiseur.dictionary.hunspell.codec.model.aff.AffixRule;
import re.belv.croiseur.dictionary.hunspell.codec.model.aff.ThreePartsCompoundFlags;
import re.belv.croiseur.dictionary.hunspell.codec.model.common.Flag;
import re.belv.croiseur.dictionary.hunspell.codec.parser.common.FlagType;
import re.belv.croiseur.dictionary.hunspell.codec.parser.common.ParserException;

/** {@link Aff} builder. */
final class AffBuilder {

    /** Affix class builders, access by flag identifying the affix class. */
    private final Map<Flag, AffixClassBuilder> affixClassBuilders;

    /** The declared encoding. */
    private Charset encoding;

    /** Flag type. */
    private FlagType flagType;

    /** The compound flag, if any, {@code null} otherwise. */
    private Flag compoundFlag;

    /** The builder for {@link ThreePartsCompoundFlags}. */
    private ThreePartsCompoundFlagsBuilder threePartsCompoundFlagsBuilder;

    /** Constructs an instance. */
    AffBuilder() {
        affixClassBuilders = new HashMap<>();
        encoding = StandardCharsets.US_ASCII;
        flagType = FlagType.SINGLE_ASCII;
        threePartsCompoundFlagsBuilder = new ThreePartsCompoundFlagsBuilder();
    }

    /**
     * Add an {@link AffixClassHeader} to this builder
     *
     * @param affixClassHeader the affix header
     * @return this builder
     */
    AffBuilder addAffixClassHeader(final AffixClassHeader affixClassHeader) {
        affixClassBuilder(affixClassHeader.flag()).addHeader(affixClassHeader);
        return this;
    }

    /**
     * Set the {@link FlagType} to this builder
     *
     * @param aFlagType the flag type
     * @return this builder
     */
    AffBuilder setFlagType(final FlagType aFlagType) {
        flagType = aFlagType;
        return this;
    }

    /**
     * Add an {@link AffixRule} to the model under construction.
     *
     * @param affixRule the affix rule
     * @return this builder
     */
    AffBuilder addAffixRule(final AffixRule affixRule) {
        affixClassBuilder(affixRule.flag()).addRule(affixRule);
        return this;
    }

    /**
     * Sets the compound flag.
     *
     * @param compoundFlagArg the parsed coumpound flag
     * @return this builder
     */
    AffBuilder setCompoundFlag(final Flag compoundFlagArg) {
        compoundFlag = compoundFlagArg;
        return this;
    }

    /**
     * Sets the {@code COMPOUNDBEGINFLAG} value.
     *
     * @param compoundBeginFlag the {@code COMPOUNDBEGINFLAG} value
     * @return this builder
     */
    AffBuilder setCompoundBeginFlag(final Flag compoundBeginFlag) {
        threePartsCompoundFlagsBuilder.setBeginFlag(compoundBeginFlag);
        return this;
    }

    /**
     * Sets the {@code COMPOUNDMIDDLEFLAG} value.
     *
     * @param compoundMiddleFlag the {@code COMPOUNDMIDDLEFLAG} value
     * @return this builder
     */
    AffBuilder setCompoundMiddleFlag(final Flag compoundMiddleFlag) {
        threePartsCompoundFlagsBuilder.setMiddleFlag(compoundMiddleFlag);
        return this;
    }

    /**
     * Sets the {@code COMPOUNDENDFLAG} value.
     *
     * @param compoundEndFlag the {@code COMPOUNDENDFLAG} value
     * @return this builder
     */
    AffBuilder setCompoundEndFlag(final Flag compoundEndFlag) {
        threePartsCompoundFlagsBuilder.setEndFlag(compoundEndFlag);
        return this;
    }

    /**
     * Sets the encoding.
     *
     * @param encodingArg the parsed encoding
     * @return this builder
     */
    AffBuilder setEncoding(final Charset encodingArg) {
        encoding = encodingArg;
        return this;
    }

    /**
     * Build the {@link Aff}
     *
     * @return the {@link Aff} model of the parsed file
     * @throws ParserException if the parsed ".aff" presents a semantic error
     */
    Aff build() throws ParserException {
        final List<AffixClass> affixClasses = new ArrayList<>();
        for (final AffixClassBuilder affixClassBuilder : affixClassBuilders.values()) {
            affixClasses.add(affixClassBuilder.build());
        }
        final Optional<Flag> optCompoundFlag = Optional.ofNullable(compoundFlag);
        final Optional<ThreePartsCompoundFlags> optThreePartsCompoundFlags = threePartsCompoundFlagsBuilder.build();
        return new Aff(encoding, flagType, affixClasses, optCompoundFlag, optThreePartsCompoundFlags);
    }

    /**
     * Returns the flag type.
     *
     * <p>Flag type is an information provided by the Aff file but is actually necessary to correctly parses some
     * options of the Aff file.
     *
     * <p>Assuming the flag type is always put at the beginning of the document and parsed first, this getter allows the
     * following parsers to know the flag type to use.
     *
     * @return the flag type
     */
    FlagType flagType() {
        return flagType;
    }

    /**
     * Return the {@link AffixClassBuilder} associated to given affix name; Create it if necessary.
     *
     * @param affixName the affix name
     * @return the associated {@link AffixClassBuilder}
     */
    private AffixClassBuilder affixClassBuilder(final Flag affixName) {
        return affixClassBuilders.computeIfAbsent(affixName, name -> new AffixClassBuilder());
    }
}
