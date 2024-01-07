/*
 * SPDX-FileCopyrightText: 2024 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.sat;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * The acceptable values for crossword cells.
 * <p>
 * Each alphabet letter is given an index between 0 and ({@link #letterCount()} - 1).
 */
final class Alphabet {

    /** The accepted letters. */
    private static final int[] LETTERS = IntStream.rangeClosed('A', 'Z').toArray();

    /** Private constructor to prevent instantiation, static methods only. */
    private Alphabet() {
        // Nothing to do.
    }

    /**
     * Returns {@code true} iff the given letter is part of the alphabet.
     *
     * @param value the letter
     * @return {@code true} iff the given letter is part of the alphabet
     */
    static boolean contains(final char value) {
        return letterIndex(value) >= 0;
    }

    /**
     * Returns the letter index in the alphabet for the given letter.
     *
     * @param value the letter
     * @return the letter index in the alphabet for the given letter or a negative value if letter
     * is not part of the alphabet
     */
    static int letterIndex(final char value) {
        return Arrays.binarySearch(LETTERS, value);
    }

    /**
     * Returns the letter at given letter index in the alphabet.
     *
     * @param letterIndex the letter index in the alphabet
     * @return the corresponding letter
     * @throws IndexOutOfBoundsException if given letter index is not between 0 and
     *                                   {@link #letterCount()} - 1
     */
    static char letterAt(final int letterIndex) {
        return (char) LETTERS[letterIndex];
    }

    /**
     * The number of letters of the alphabet.
     *
     * @return the number of letters of the alphabet
     */
    static int letterCount() {
        return LETTERS.length;
    }
}
