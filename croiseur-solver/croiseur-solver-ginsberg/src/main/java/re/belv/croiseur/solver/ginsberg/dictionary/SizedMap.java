/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.dictionary;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A map with a max size.
 *
 * <p>Eldest entry will be removed if necessary to ensure size does not exceed max size.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
final class SizedMap<K, V> extends LinkedHashMap<K, V> {

    /** The max size. */
    private final int maxSize;

    /**
     * Constructs an instance.
     *
     * @param maxSizeArg the max size for this map
     */
    SizedMap(final int maxSizeArg) {
        maxSize = maxSizeArg;
    }

    @Override
    protected boolean removeEldestEntry(final Map.Entry eldest) {
        return size() > maxSize;
    }
}
