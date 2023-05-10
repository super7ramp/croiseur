/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.api.dictionary;

/**
 * Unique identification for a dictionary.
 *
 * @param providerName   the dictionary provider name
 * @param dictionaryName the dictionary name
 */
public record DictionaryIdentifier(String providerName, String dictionaryName) {
    // Nothing to add.
}
