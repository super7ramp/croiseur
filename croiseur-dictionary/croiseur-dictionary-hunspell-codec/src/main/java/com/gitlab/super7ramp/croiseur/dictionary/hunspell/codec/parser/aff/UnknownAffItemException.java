/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec.parser.aff;

import com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec.parser.common.ParserException;

final class UnknownAffItemException extends ParserException {

    private static final String MESSAGE = "Unknown item: ";

    UnknownAffItemException(final String message) {
        super(MESSAGE + message);
    }
}
