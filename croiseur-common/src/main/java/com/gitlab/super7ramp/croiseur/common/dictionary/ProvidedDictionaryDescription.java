/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.common.dictionary;

/**
 * Gathers provider and dictionary information.
 *
 * @param providerName provider name
 * @param dictionary   dictionary description
 */
public record ProvidedDictionaryDescription(String providerName, DictionaryDescription dictionary) {

    /**
     * Returns the {@link DictionaryKey} from this description.
     *
     * @return the {@link DictionaryKey} from this description
     */
    public DictionaryKey toDictionaryKey() {
        return new DictionaryKey(providerName, dictionary.name(), dictionary.locale());
    }
}
