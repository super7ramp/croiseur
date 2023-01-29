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
 * @param begin the dictionary entry for the begin part
 * @param end   the dictionary entry for the end part
 */
record BeginEndCompound(DicEntry begin, DicEntry end) {

    /**
     * Returns the flags of left part dictionary entry.
     * <p>
     * Shortcut for {@code left().flags()}.
     *
     * @return the flags of left part dictionary entry
     */
    Collection<Flag> beginFlags() {
        return begin.flags();
    }

    /**
     * Returns the flags of right part dictionary entry.
     * <p>
     * Shortcut for {@code right().flags()}.
     *
     * @return the flags of right part dictionary entry
     */
    Collection<Flag> endFlags() {
        return end.flags();
    }

    /**
     * Returns the compounded form.
     *
     * @return the compounded form
     */
    String word() {
        // FIXME this assumes left-to-right language
        return begin.word() + end.word();
    }
}
