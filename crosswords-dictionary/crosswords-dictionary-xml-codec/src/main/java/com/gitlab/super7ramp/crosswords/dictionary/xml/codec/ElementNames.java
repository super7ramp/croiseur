/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.xml.codec;

/**
 * Constant element names.
 */
final class ElementNames {

    /** {@code <locale>}'s name. */
    static final String LOCALE = "locale";

    /** {@code <name>}'s name. */
    static final String NAME = "name";

    /** {@code <description>}'s name. */
    static final String DESCRIPTION = "description";

    /** {@code <words>}' name. */
    static final String WORDS = "words";

    /** {@code <word>}' name. */
    static final String WORD = "word";

    /** Private constructor to prevent instantiation. */
    private ElementNames() {
        // Nothing to do.
    }
}
