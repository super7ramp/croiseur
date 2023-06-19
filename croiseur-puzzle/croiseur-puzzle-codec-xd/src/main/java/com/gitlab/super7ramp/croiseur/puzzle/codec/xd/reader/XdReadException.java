/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.codec.xd.reader;

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

    /**
     * Constructs an instance.
     *
     * @param message the detail message
     */
    XdReadException(final String message) {
        super(message);
    }
}
