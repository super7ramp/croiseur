/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.common.Flag;

import java.util.Collection;

/**
 * Represents a parsed affix class.
 *
 * @param header the affix class header
 * @param rules  the affix class rules
 */
public record AffixClass(AffixClassHeader header, Collection<AffixRule> rules) {

    /**
     * Returns the flag identifying this affix class.
     * <p>
     * Just a shortcut for {@code header().flag()}.
     *
     * @return the flag identifying this affix class
     */
    public Flag flag() {
        return header.flag();
    }

    /**
     * Returns whether prefixes and suffixes of this class can be applied simultaneously.
     * <p>
     * Just a shortcut for {@code header().crossProduct()}.
     *
     * @return whether prefixes and suffixes of this class can be applied simultaneously
     */
    public boolean crossProduct() {
        return header.crossProduct();
    }

    /**
     * Returns the affix kind of this class.
     * <p>
     * Just a shortcut for {@code header().kind()}.
     *
     * @return the affix kind of this class
     */
    public AffixKind kind() {
        return header.kind();
    }

}
