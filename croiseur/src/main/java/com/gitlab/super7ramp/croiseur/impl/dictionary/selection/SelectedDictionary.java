/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.dictionary.selection;

import com.gitlab.super7ramp.croiseur.common.dictionary.ProvidedDictionaryDetails;
import com.gitlab.super7ramp.croiseur.spi.dictionary.Dictionary;

import java.util.Set;

/**
 * Just another dictionary type gathering provider details, dictionary details and dictionary words.
 */
public interface SelectedDictionary {

    /**
     * Returns the dictionary details as well as its provider details.
     *
     * @return the dictionary details as well as its provider details
     */
    ProvidedDictionaryDetails details();

    /**
     * Returns the words of the dictionary
     *
     * @return the words of the dictionary
     */
    Set<String> words();

    /**
     * Creates a new {@link SelectedDictionary} wrapping the given arguments.
     *
     * @param providerName the provider name
     * @param dictionary   the actual dictionary
     * @return a new {@link SelectedDictionary} wrapping the given arguments.
     */
    static SelectedDictionary of(final String providerName, final Dictionary dictionary) {
        return new SelectedDictionary() {
            @Override
            public ProvidedDictionaryDetails details() {
                return new ProvidedDictionaryDetails(providerName, dictionary.details());
            }

            @Override
            public Set<String> words() {
                return dictionary.words();
            }
        };
    }
}