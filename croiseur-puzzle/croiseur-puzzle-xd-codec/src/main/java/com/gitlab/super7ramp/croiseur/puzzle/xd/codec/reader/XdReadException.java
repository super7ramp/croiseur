/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.xd.codec.reader;

/**
 * Abstract read exception, thrown by xd readers.
 */
abstract class XdReadException extends Exception {

    /**
     * Constructs an instance.
     *
     * @param cause the cause (which is saved for later retrieval by the getCause() method). (A null
     *              value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    XdReadException(final Throwable cause) {
        super(cause);
    }

    XdReadException(final String message) {
        super(message);
    }
}
