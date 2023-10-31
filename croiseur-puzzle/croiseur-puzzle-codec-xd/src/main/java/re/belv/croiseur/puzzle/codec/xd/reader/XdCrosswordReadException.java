/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.codec.xd.reader;

/**
 * {@link XdReadException} raised when error occurred reading the crossword.
 */
public final class XdCrosswordReadException extends XdReadException {

    /**
     * Constructs an instance.
     *
     * @param cause the cause
     */
    XdCrosswordReadException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance.
     *
     * @param message the detail message
     */
    XdCrosswordReadException(final String message) {
        super(message);
    }
}
