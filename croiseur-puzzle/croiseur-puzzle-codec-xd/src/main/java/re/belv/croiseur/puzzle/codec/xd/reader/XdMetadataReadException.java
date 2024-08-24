/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.codec.xd.reader;

/** Exception raised when failing to read metadata. */
public final class XdMetadataReadException extends XdReadException {

    /**
     * Constructs an instance.
     *
     * @param cause the cause (which is saved for later retrieval by the getCause() method). (A null value is permitted,
     *     and indicates that the cause is nonexistent or unknown.)
     */
    XdMetadataReadException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance.
     *
     * @param message the detail message
     */
    XdMetadataReadException(final String message) {
        super("Invalid metadata: " + message);
    }
}
