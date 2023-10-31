/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.model.aff;

import re.belv.croiseur.dictionary.hunspell.codec.model.common.Flag;

import java.util.Objects;

/**
 * The compounding flags specific to three-part flag-based compounding.
 *
 * @param begin  the value of {@code COMPOUNDBEGIN}
 * @param middle the value of {@code COMPOUNDMIDDLE}
 * @param end    the value of {@code COMPOUNDEND}
 */
public record ThreePartsCompoundFlags(Flag begin, Flag middle, Flag end) {

    /**
     * Performs some null checks.
     *
     * @param begin  the value of {@code COMPOUNDBEGIN}
     * @param middle the value of {@code COMPOUNDMIDDLE}
     * @param end    the value of {@code COMPOUNDEND}
     */
    public ThreePartsCompoundFlags {
        Objects.requireNonNull(begin);
        Objects.requireNonNull(middle);
        Objects.requireNonNull(end);
    }

}
