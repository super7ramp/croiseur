/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.parser.aff;

import re.belv.croiseur.dictionary.hunspell.codec.parser.common.ParserException;

final class MissingAffixNameException extends ParserException {

    private static final String MESSAGE = "Missing affix name";

    MissingAffixNameException() {
        super(MESSAGE);
    }
}
