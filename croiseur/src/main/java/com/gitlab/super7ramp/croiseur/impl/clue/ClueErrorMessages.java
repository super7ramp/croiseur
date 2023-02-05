/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.clue;

/**
 * Some clue error messages.
 */
final class ClueErrorMessages {

    /** Error message to publish when no clue provider matching the request is found. */
    static final String NO_CLUE_PROVIDER_ERROR_MESSAGE = "No clue provider found";

    /** Prevents instantiation. */
    private ClueErrorMessages() {
        // Nothing to do.
    }
}
