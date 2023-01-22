/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.dic;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.common.Flag;

import java.util.Collection;

/**
 * A parsed dictionary entry.
 */
public record DicEntry(boolean isForbidden, String word, Collection<Flag> flags) {
    // Nothing to add.
}
