/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec.util;

/**
 * Just a pair of elements.
 *
 * @param left  the left part
 * @param right the right part
 * @param <L>   the left part type
 * @param <R>   the right part type
 */
public record Pair<L, R>(L left, R right) {
    // Nothing to add
}
