/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.wordforms;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.common.Flag;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.dic.DicEntry;

import java.util.Collection;

/**
 * A simple compound composed of two dictionary entries.
 *
 * @param left  the dictionary entry for the left part
 * @param right the dictionary entry for the right part
 */
record SimpleCompound(DicEntry left, DicEntry right) {

    /**
     * Returns the flags of left part dictionary entry.
     * <p>
     * Shortcut for {@code left().flags()}.
     *
     * @return the flags of left part dictionary entry
     */
    Collection<Flag> leftFlags() {
        return left.flags();
    }

    /**
     * Returns the flags of right part dictionary entry.
     * <p>
     * Shortcut for {@code right().flags()}.
     *
     * @return the flags of right part dictionary entry
     */
    Collection<Flag> rightFlags() {
        return right.flags();
    }

    /**
     * Returns the compounded form.
     *
     * @return the compounded form
     */
    String word() {
        return left.word() + right.word();
    }
}
