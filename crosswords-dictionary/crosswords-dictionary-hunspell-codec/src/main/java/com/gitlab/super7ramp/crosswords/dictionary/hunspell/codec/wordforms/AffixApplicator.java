/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.wordforms;

import java.util.Optional;
import java.util.function.Function;

interface AffixApplicator extends Function<String, Optional<String>> {
    // Marker interface
}
