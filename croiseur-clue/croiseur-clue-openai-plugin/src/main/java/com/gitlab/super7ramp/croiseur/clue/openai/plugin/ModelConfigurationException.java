/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.clue.openai.plugin;

/**
 * Configuration exception raised in case of missing configuration.
 */
public final class ModelConfigurationException extends RuntimeException {

    /**
     * Constructs an instance.
     */
    ModelConfigurationException() {
        this(null);
    }

    /**
     * Constructs an instance with the specified cause.
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A {@code null} value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     */
    ModelConfigurationException(final Throwable cause) {
        super(cause);
    }
}
