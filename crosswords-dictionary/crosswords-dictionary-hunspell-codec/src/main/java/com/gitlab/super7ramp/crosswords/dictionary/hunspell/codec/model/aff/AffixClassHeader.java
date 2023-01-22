/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.common.Flag;

/**
 * An affix class header as parsed from an ".aff" file.
 *
 * @param kind          the kind of affix (prefix/suffix)
 * @param flag          the affix flag
 * @param crossProduct  whether other affix of different kind can be applied when this affix is
 *                      applied
 * @param numberOfRules the number or rules under this header
 */
public record AffixClassHeader(AffixKind kind, Flag flag, boolean crossProduct, int numberOfRules) {
    // Nothing to add.
}
