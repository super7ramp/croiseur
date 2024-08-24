/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.wordforms;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;
import re.belv.croiseur.dictionary.hunspell.codec.model.dic.DicEntry;

/** Creates compounds from the given entries. */
interface Compounder extends Function<Collection<DicEntry>, Stream<String>> {
    // Marker interface
}
