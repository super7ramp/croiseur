/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.common.dictionary;

/**
 * Gathers provider and dictionary information.
 *
 * @param provider   provider description
 * @param dictionary dictionary description
 */
public record ProvidedDictionaryDescription(DictionaryProviderDescription provider,
                                            DictionaryDescription dictionary) {

    /**
     * Returns the {@link DictionaryKey} from this description.
     *
     * @return the {@link DictionaryKey} from this description
     */
    public DictionaryKey toDictionaryKey() {
        return new DictionaryKey(provider.name(), dictionary.name(), dictionary.locale());
    }
}
