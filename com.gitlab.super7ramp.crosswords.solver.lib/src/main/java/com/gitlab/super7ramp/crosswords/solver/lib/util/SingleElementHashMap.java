package com.gitlab.super7ramp.crosswords.solver.lib.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Hashmap with a single element.
 */
public final class SingleElementHashMap<K, V> extends LinkedHashMap<K, V> {

    /**
     * Constructor.
     */
    public SingleElementHashMap() {
        super(1);
    }

    @Override
    protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
        return size() > 1;
    }
}
