/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.parser.common;

/**
 * Parser exception.
 */
public abstract class ParserException extends Exception {

    /**
     * Constructs an instance.
     *
     * @param message the detail message
     */
    protected ParserException(final String message) {
        super(message);
    }
}
