/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.dictionary;

import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryProviderDetails;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

/**
 * {@link Dictionary} provider.
 */
public interface DictionaryProvider {

    /**
     * Returns details about this dictionary provider.
     *
     * @return a description of this dictionary provider
     */
    DictionaryProviderDetails details();

    /**
     * Retrieves the available dictionaries.
     *
     * @return the available dictionaries found in given paths; if no path is given,
     * implementation may look for available dictionaries at default locations (e.g. built-in
     * dictionaries, well-known system paths, at the discretion of implementation).
     */
    Collection<Dictionary> get();

    /**
     * Retrieves the first available dictionary, if any.
     *
     * @return the first available dictionary, if any, found in given paths; if no path is given,
     * implementation may look for available dictionaries at default locations (e.g. built-in
     * dictionaries, well-known system paths, at the discretion of implementation).
     */
    default Optional<Dictionary> getFirst() {
        final Iterator<Dictionary> it = get().iterator();
        final Optional<Dictionary> first;
        if (it.hasNext()) {
            first = Optional.of(it.next());
        } else {
            first = Optional.empty();
        }
        return first;
    }
}
