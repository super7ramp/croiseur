/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.xml.codec;

import java.util.List;

/**
 * A dictionary.
 *
 * @param header the header
 * @param words the words
 */
public record Dictionary(DictionaryHeader header, List<String> words) {
    // Nothing to add.
}
