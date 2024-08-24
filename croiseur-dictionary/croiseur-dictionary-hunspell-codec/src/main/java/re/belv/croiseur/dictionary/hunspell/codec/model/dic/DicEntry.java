/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.model.dic;

import java.util.Collection;
import re.belv.croiseur.dictionary.hunspell.codec.model.common.Flag;

/**
 * A dictionary entry.
 *
 * @param isForbidden whether the word is forbidden
 * @param word the word
 * @param flags the flags identifying the rules applicable to this entry
 */
public record DicEntry(boolean isForbidden, String word, Collection<Flag> flags) {

    /**
     * Returns {@code true} iff this entry references the given flag.
     *
     * <p>Shortcut for {@code flags().contains(flag)}.
     *
     * @param flag the flag to check
     * @return {@code true} iff this entry references the given flag
     */
    public boolean isFlaggedWith(final Flag flag) {
        return flags.contains(flag);
    }
}
