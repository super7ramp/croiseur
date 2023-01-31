/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec.util;

/**
 * A triplet of elements.
 *
 * @param left   the left value
 * @param middle the middle value
 * @param right  the right value
 * @param <L>    the left value type
 * @param <M>    the right value type
 * @param <R>    the right value type
 */
public record Triplet<L, M, R>(L left, M middle, R right) {
    // Nothing to add
}
