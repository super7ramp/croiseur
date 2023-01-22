/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.dic;

import java.util.Collection;

/**
 * Represents a parsed ".dic" file.
 */
public record Dic(Collection<DicEntry> entries) {
    // Nothing to add.
}
