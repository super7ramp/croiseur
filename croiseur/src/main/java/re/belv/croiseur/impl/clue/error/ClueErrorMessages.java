/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.clue.error;

/**
 * Some clue error messages.
 */
public final class ClueErrorMessages {

    /** Error message to publish when no clue is returned by clue provider. */
    public static final String NO_CLUE = "Couldn't find any acceptable clues";

    /** Error message to publish when no clue provider matching the request is found. */
    public static final String NO_CLUE_PROVIDER = "No clue provider found";

    /** Error message to publish when clue provider fails with a runtime exception. */
    public static final String CLUE_PROVIDER_FAILED = "Clue provider %s failed with the following message: %s";

    /** Prevents instantiation. */
    private ClueErrorMessages() {
        // Nothing to do.
    }
}
