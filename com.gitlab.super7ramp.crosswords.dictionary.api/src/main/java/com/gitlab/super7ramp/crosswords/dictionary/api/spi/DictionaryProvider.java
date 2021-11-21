package com.gitlab.super7ramp.crosswords.dictionary.api.spi;

import com.gitlab.super7ramp.crosswords.dictionary.api.Dictionary;

import java.net.URL;
import java.util.Collection;

/**
 * {@link Dictionary} provider.
 */
public interface DictionaryProvider {

    enum Quality {
        DEBUG_ONLY,
        BAD,
        MEDIUM,
        GOOD
    }

    /**
     * @return {@link Dictionary dictionaries} found in given search paths
     */
    Collection<Dictionary> get(final URL... dictionaryPaths);

    /**
     * @return an indication of the {@link Quality} of the dictionary provider; This may be used by service loader as
     * criterion for selecting this implementation
     */
    Quality quality();
}
