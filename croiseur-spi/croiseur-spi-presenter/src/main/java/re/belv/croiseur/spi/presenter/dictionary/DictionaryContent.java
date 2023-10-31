/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.spi.presenter.dictionary;

import re.belv.croiseur.common.dictionary.ProvidedDictionaryDetails;

import java.util.Set;

/**
 * The words of a dictionary for presentation purposes.
 *
 * @param details details about the dictionary and its provider
 * @param words   all the words inside the dictionary
 */
public record DictionaryContent(ProvidedDictionaryDetails details, Set<String> words) {
    // Nothing to add.
}
