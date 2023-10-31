/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.parser.dic;

import re.belv.croiseur.dictionary.hunspell.codec.parser.common.ParserException;

final class InvalidDicEntryException extends ParserException {

    private static final String MESSAGE = "Invalid dic entry: ";

    InvalidDicEntryException(final String invalidEntry) {
        super(MESSAGE + invalidEntry);
    }
}
