/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

import java.util.Collection;

/**
 * Represents a parsed affix class.
 *
 * @param header the affix class header
 * @param rules  the affix class rules
 */
public record AffixClass(AffixClassHeader header, Collection<AffixRule> rules) {
    // Nothing to add.
}
