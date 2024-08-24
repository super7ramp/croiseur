/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.dictionary.error;

/** Some dictionary error messages. */
public final class DictionaryErrorMessages {

    /** Error message to publish when no dictionary matching the request is found. */
    public static final String NO_DICTIONARY_ERROR_MESSAGE = "No dictionary found";

    /** Error message to publish when an ambiguous request is received. */
    public static final String AMBIGUOUS_REQUEST_ERROR_MESSAGE =
            "Ambiguous request: Found " + "matching dictionaries for several providers";

    /** Prevents instantiation. */
    private DictionaryErrorMessages() {
        // Nothing to do.
    }
}
