/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.parser.aff;

import re.belv.croiseur.dictionary.hunspell.codec.parser.common.ParserException;

final class UnknownAffItemException extends ParserException {

    private static final String MESSAGE = "Unknown item: ";

    UnknownAffItemException(final String message) {
        super(MESSAGE + message);
    }
}
