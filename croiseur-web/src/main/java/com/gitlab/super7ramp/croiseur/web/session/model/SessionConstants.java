/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.session.model;

/**
 * Some constants for session models.
 */
final class SessionConstants {

    /** Default request timeout in milliseconds. */
    static final long DEFAULT_TIMEOUT = 5_000;

    /** Private constructor to prevent instantian. Static fields only. */
    private SessionConstants() {
        // Nothing to do.
    }
}
