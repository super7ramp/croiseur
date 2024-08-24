/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.repository.filesystem.plugin;

/** Thrown when conversion between domain and persisted objects fails. */
final class PuzzleConversionException extends Exception {

    /**
     * Constructs an instance.
     *
     * @param message the detail message
     */
    PuzzleConversionException(final String message) {
        super(message);
    }

    /**
     * Constructs an instance.
     *
     * @param message the detail message
     * @param cause the cause
     */
    PuzzleConversionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
