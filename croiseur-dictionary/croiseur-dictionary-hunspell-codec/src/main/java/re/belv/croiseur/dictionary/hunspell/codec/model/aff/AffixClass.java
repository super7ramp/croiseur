/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.model.aff;

import java.util.Collection;
import java.util.Objects;
import re.belv.croiseur.dictionary.hunspell.codec.model.common.Flag;

/**
 * Represents a parsed affix class.
 *
 * @param header the affix class header
 * @param rules  the affix class rules
 */
public record AffixClass(AffixClassHeader header, Collection<AffixRule> rules) {

    /**
     * Performs some null checks.
     *
     * @param header the affix class header
     * @param rules  the affix class rules
     */
    public AffixClass {
        Objects.requireNonNull(header);
        Objects.requireNonNull(rules);
    }

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
