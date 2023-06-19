/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.xd.codec.reader;

/**
 * {@link XdReadException} raised when error occurred reading the crossword.
 */
public final class XdCrosswordReadException extends XdReadException {

    /**
     * Constructs an instance.
     *
     * @param message the detail message
     */
    XdCrosswordReadException(final String message) {
        super(message);
    }
}
