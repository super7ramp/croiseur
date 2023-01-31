/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.common.dictionary;

import java.util.Locale;

/**
 * A dictionary description.
 *
 * @param name   the name of the dictionary
 * @param locale the locale of the dictionary
 */
public record DictionaryDescription(String name, Locale locale) {

    /**
     * Creates a dictionary description for an unknown dictionary.
     *
     * @return a dictionary description for an unknown dictionary
     */
    public static DictionaryDescription unknown() {
        return new DictionaryDescription("<Unknown dictionary>", Locale.ENGLISH);
    }
}
