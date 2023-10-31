/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.parser.dic;

import re.belv.croiseur.dictionary.hunspell.codec.parser.common.ParserException;

final class MissingEstimatedNumberOfEntriesException extends ParserException {

    private static final String MESSAGE = "Estimated number of entries is missing";

    MissingEstimatedNumberOfEntriesException() {
        this("");
    }

    MissingEstimatedNumberOfEntriesException(final String message) {
        super(MESSAGE + ": " + message);
    }
}
