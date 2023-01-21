/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.Flag;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.FlagType;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.ParserException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * {@link Aff} builder.
 */
final class AffBuilder {

    /** Affix class builders, access by flag identifying the affix class. */
    private final Map<Flag, AffixClassBuilder> affixClassBuilders;

    /** Flag type. */
    private FlagType flagType;

    /** The compound flag, if any, {@code null} otherwise. */
    private Flag compoundFlag;

    /**
     * Constructs an instance.
     */
    AffBuilder() {
        affixClassBuilders = new HashMap<>();
        flagType = FlagType.SINGLE_ASCII;
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
        return new Aff(flagType, affixClasses, Optional.ofNullable(compoundFlag));
    }

    /**
     * Returns the flag type.
     * <p>
     * Flag type is an information provided by the Aff file but is actually necessary to correctly
     * parses some options of the Aff file.
     * <p>
     * Assuming the flag type is always put at the beginning of the document and parsed first,
     * this getter allows the following parsers to know the flag type to use.
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
