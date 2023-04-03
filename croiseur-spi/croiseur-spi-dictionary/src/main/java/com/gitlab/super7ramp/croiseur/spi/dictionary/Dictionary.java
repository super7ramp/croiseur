/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.dictionary;

import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryDescription;

import java.util.Set;

/**
 * A dictionary.
 */
public interface Dictionary {

    /**
     * Returns a description of this dictionary.
     *
     * @return a description of this dictionary
     */
    DictionaryDescription description();

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
     *     <li>should be fast to iterate on and with a deterministic order in
     *     order to have as fast as possible and deterministic results. A typical implementation
     *     would use a LinkedHashSet.</li>
     * </ul>
     * @see <a href="https://openjdk.org/jeps/431">SequencedSet (Java 21)</a>, which describes
     * better than Set the expected behaviour of the returned collection
     */
    Set<String> words();

}
