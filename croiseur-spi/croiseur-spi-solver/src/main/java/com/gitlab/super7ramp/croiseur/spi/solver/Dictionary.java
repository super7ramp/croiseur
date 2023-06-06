/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.solver;

import java.util.Collection;

/**
 * Dictionary interface (to be provided).
 */
public interface Dictionary {

    /**
     * Returns the dictionary words.
     *
     * @return the dictionary words as a {@link Collection}
     * @implSpec the returned strings:
     * <ul>
     *     <li>shall only contain characters which can be represented as a primitive char value
     *     (a single 16-bit Unicode character, range {@code u0000} to {@code uFFFF} inclusive)
     *     <li>shall not contain the character ' ' (space)
     *     <li>shall not contain the character '#' (number sign}
     *     <li>should be either all uppercase or all lowercase but not a mix of the two (processing
     *     is case sensitive)
     *     <li>should be unique in order to avoid redundant searches/duplicate words in solutions
     *     <li>should be fast to iterate on and with a deterministic order in order to have as fast
     *     as possible and deterministic results.
     * </ul>
     *  A typical implementation would use a LinkedHashSet.
     */
    Collection<String> words();
}
