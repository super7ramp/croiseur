/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg;

import java.util.Set;

/**
 * Dictionary interface (to be provided).
 */
public interface Dictionary {

    /**
     * Returns the dictionary words.
     *
     * @return the dictionary words as a {@link Set}
     * @implSpec the returned strings:
     * <ul>
     *     <li>shall only contain characters which can be represented as a primitive char value
     *     (a single 16-bit Unicode character, range {@code u0000} to {@code uFFFF} inclusive)
     *     <li>shall not contain the character ' ' (space)
     *     <li>shall not contain the character '#' (number sign}
     *     <li>should be either all uppercase or all lowercase but not a mix of the two (processing
     *     is case sensitive)</li>
     * </ul>
     */
    Set<String> words();
}
