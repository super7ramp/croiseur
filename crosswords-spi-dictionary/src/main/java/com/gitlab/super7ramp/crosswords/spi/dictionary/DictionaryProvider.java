package com.gitlab.super7ramp.crosswords.spi.dictionary;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

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
     * @return retrieve available dictionaries from given paths; if no path is given,
     * implementation may look for available dictionaries at default locations (e.g. built-in
     * dictionaries, well-known system paths, at the discretion of implementation).
     */
    Collection<Dictionary> get(final URL... dictionaryPaths);

    /**
     * Retrieves the first available dictionary, if any.
     *
     * @return retrieve available dictionary from given paths; if no path is given,
     * implementation may look for available dictionaries at default locations (e.g. built-in
     * dictionaries, well-known system paths, at the discretion of implementation).
     */
    default Optional<Dictionary> getFirst(final URL... dictionaryPaths) {
        final Iterator<Dictionary> it = get(dictionaryPaths).iterator();
        if (it.hasNext()) {
            return Optional.of(it.next());
        }
        return Optional.empty();
    }

    /**
     * @return an indication of the {@link Quality} of the dictionary provider; This may be used
     * by service loader as criterion for selecting this implementation
     */
    Quality quality();
}