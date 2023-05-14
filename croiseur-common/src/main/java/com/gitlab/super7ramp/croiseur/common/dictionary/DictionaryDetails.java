/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.common.dictionary;

import java.util.Locale;

/**
 * Details about a dictionary.
 *
 * @param name   the name of the dictionary
 * @param locale the locale of the dictionary
 */
public record DictionaryDetails(String name, Locale locale) {

    /**
     * Creates a dictionary description for an unknown dictionary.
     *
     * @return a dictionary description for an unknown dictionary
     */
    public static DictionaryDetails unknown() {
        return new DictionaryDetails("<Unknown dictionary>", Locale.ENGLISH);
    }
}
