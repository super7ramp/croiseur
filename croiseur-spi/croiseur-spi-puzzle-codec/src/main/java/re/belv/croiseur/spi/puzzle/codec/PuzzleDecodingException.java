/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.spi.puzzle.codec;

/**
 * Exception raised whenever something goes wrong when decoding a puzzle.
 */
public final class PuzzleDecodingException extends Exception {
    /**
     * Constructs an instance.
     *
     * @param cause the cause; a {@code null} value is permitted
     */
    public PuzzleDecodingException(final Throwable cause) {
        super(cause);
    }
    /**
     * Constructs an instance.
     *
     * @param message the detail message
     */
    public PuzzleDecodingException(final String message) {
        super(message);
    }
}
