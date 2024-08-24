/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.dictionary.selection;

import java.util.Set;
import re.belv.croiseur.common.dictionary.ProvidedDictionaryDetails;
import re.belv.croiseur.spi.dictionary.Dictionary;

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
