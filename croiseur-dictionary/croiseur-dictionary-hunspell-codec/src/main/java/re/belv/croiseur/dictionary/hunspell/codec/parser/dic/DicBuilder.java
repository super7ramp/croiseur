/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.parser.dic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import re.belv.croiseur.dictionary.hunspell.codec.model.dic.Dic;
import re.belv.croiseur.dictionary.hunspell.codec.model.dic.DicEntry;

/** {@link Dic} builder. */
final class DicBuilder {

    /** The list of entries being built. */
    private final List<DicEntry> entries;

    /**
     * Constructor.
     *
     * @param estimatedNumberOfEntries the estimated number of entries
     */
    DicBuilder(final int estimatedNumberOfEntries) {
        entries = new ArrayList<>(estimatedNumberOfEntries);
    }

    /**
     * Add a {@link DicEntry} to this builder.
     *
     * @param entry the entry to add
     * @return this builder
     */
    DicBuilder add(DicEntry entry) {
        entries.add(entry);
        return this;
    }

    /**
     * Build a {@link Dic}.
     *
     * @return the built {@link Dic}
     */
    Dic build() {
        return new Dic(Collections.unmodifiableList(entries));
    }
}
