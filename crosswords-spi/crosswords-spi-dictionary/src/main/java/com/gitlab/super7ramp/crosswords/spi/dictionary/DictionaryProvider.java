package com.gitlab.super7ramp.crosswords.spi.dictionary;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

/**
 * {@link Dictionary} provider.
 */
public interface DictionaryProvider extends DictionaryProviderDescription {

    /**
     * Retrieves the available dictionaries.
     *
     * @return the available dictionaries found in given paths; if no path is given,
     * implementation may look for available dictionaries at default locations (e.g. built-in
     * dictionaries, well-known system paths, at the discretion of implementation).
     */
    Collection<Dictionary> get(final URL... dictionaryPaths);

    /**
     * Retrieves the first available dictionary, if any.
     *
     * @return the first available dictionary, if any, found in given paths; if no path is given,
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
}
