/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.common.Flag;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.FlagType;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a parsed ".aff" file.
 *
 * @param encoding                the declared encoding
 * @param flagType                the flag type
 * @param affixClasses            the affix classes
 * @param compoundFlag            the compound flag, if any
 * @param threePartsCompoundFlags the compound begin/middle/end flags, if any
 */
public record Aff(Charset encoding, FlagType flagType, Collection<AffixClass> affixClasses,
                  Optional<Flag> compoundFlag,
                  Optional<ThreePartsCompoundFlags> threePartsCompoundFlags) {

    /**
     * Performs some null checks.
     *
     * @param encoding                the declared encoding
     * @param flagType                the flag type
     * @param affixClasses            the affix classes
     * @param compoundFlag            the compound flag, if any
     * @param threePartsCompoundFlags the compound begin/middle/end flags, if any
     */
    public Aff {
        Objects.requireNonNull(encoding);
        Objects.requireNonNull(flagType);
        Objects.requireNonNull(affixClasses);
        Objects.requireNonNull(compoundFlag);
        Objects.requireNonNull(threePartsCompoundFlags);
    }

}
