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
     * @return name of this dictionary provider
     */
    String name();

    /**
     * @return a description of this dictionary provider
     */
    String description();

    /**
     * Retrieve the available dictionaries.
     *
     * @return retrieve available dictionaries from given paths; if no path is given, implementation may look for
     * available dictionaries at default locations (e.g. built-in dictionaries, well-known system paths, at the
     * discretion of implementation).
     */
    Collection<Dictionary> get(final URL... dictionaryPaths);

    /**
     * @return an indication of the {@link Quality} of the dictionary provider; This may be used by service loader as
     * criterion for selecting this implementation
     */
    Quality quality();
}
