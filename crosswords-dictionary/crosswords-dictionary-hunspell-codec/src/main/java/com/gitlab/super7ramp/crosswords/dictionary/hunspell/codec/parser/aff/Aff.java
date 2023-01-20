/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.FlagType;

import java.util.Collection;

/**
 * Represents a parsed ".aff" file.
 */
public record Aff(FlagType flagType, Collection<AffixClass> affixClasses) {
    // Nothing to add.
}
