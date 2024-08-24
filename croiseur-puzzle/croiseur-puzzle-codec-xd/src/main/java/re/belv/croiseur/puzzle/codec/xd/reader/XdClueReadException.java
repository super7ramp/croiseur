/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.codec.xd.reader;

/**
 * {@link XdReadException} relevant to the clues section.
 */
public final class XdClueReadException extends XdReadException {

    /**
     * Constructs an instance.
     *
     * @param message the detail message
     */
    XdClueReadException(final String message) {
        super("Invalid clue: " + message);
    }
}
