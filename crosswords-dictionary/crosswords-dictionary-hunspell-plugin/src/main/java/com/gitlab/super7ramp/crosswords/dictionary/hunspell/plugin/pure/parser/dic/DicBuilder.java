package com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.dic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link Dic} builder.
 */
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