package com.gitlab.super7ramp.crosswords.dictionary.internal;

import com.gitlab.super7ramp.crosswords.dictionary.api.Dictionary;
import com.gitlab.super7ramp.crosswords.dictionary.api.spi.DictionaryProvider;

import java.net.URL;
import java.util.Collection;

public class InternalDictionaryProvider implements DictionaryProvider {

    /**
     * Constructor.
     */
    public InternalDictionaryProvider() {
        // Nothing to do.
    }

    @Override
    public Collection<Dictionary> get(final URL... dictionaryPaths) {
        return null;
    }

    @Override
    public Quality quality() {
        return Quality.GOOD;
    }
}
