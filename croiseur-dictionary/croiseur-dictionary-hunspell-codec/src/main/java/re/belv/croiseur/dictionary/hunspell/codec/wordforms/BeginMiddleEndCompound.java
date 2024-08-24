/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.wordforms;

import java.util.Collection;
import re.belv.croiseur.dictionary.hunspell.codec.model.common.Flag;
import re.belv.croiseur.dictionary.hunspell.codec.model.dic.DicEntry;

/**
 * A compound composed of three dictionary entries.
 *
 * @param begin  the dictionary entry for the beginning part
 * @param middle the dictionary entry for the middle part
 * @param end    the dictionary entry for the end part
 */
record BeginMiddleEndCompound(DicEntry begin, DicEntry middle, DicEntry end) {

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
        return begin.word() + middle.word() + end.word();
    }
}
