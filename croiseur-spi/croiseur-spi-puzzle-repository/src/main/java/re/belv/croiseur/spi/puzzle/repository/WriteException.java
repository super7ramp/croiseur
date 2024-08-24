/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.spi.puzzle.repository;

/** An exception occurring when repository cannot be written. */
public final class WriteException extends Exception {

    /**
     * Constructs an instance.
     *
     * @param message the message
     * @param cause the cause
     */
    public WriteException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an instance.
     *
     * @param message the detail message
     */
    public WriteException(final String message) {
        super(message);
    }
}
