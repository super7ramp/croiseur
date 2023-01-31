/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.presenter.dictionary;

import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.croiseur.common.dictionary.ProvidedDictionaryDescription;

import java.util.Collection;
import java.util.List;

/**
 * Dictionary-related presentation services.
 */
public interface DictionaryPresenter {

    /**
     * Presents the dictionary providers.
     *
     * @param providers the dictionary providers
     */
    void presentDictionaryProviders(final Collection<DictionaryProviderDescription> providers);

    /**
     * Presents the dictionaries.
     * <p>
     * The returned dictionaries are sorted by order of preference.
     *
     * @param dictionaries the dictionaries
     */
    void presentDictionaries(final List<ProvidedDictionaryDescription> dictionaries);

    /**
     * Presents the content of a dictionary.
     * <p>
     * Dictionary word list is given as read from the dictionary: It may or may not be sorted.
     * Presenter should sort it when sort order is important.
     *
     * @param content the dictionary content
     */
    void presentDictionaryEntries(final DictionaryContent content);

    /**
     * Presents the preferred dictionary.
     * <p>
     * Typically, this is the first dictionary returned by {@link #presentDictionaries(List)}.
     *
     * @param preferredDictionary the preferred dictionary, if any; {@code null} otherwise
     */
    void presentPreferredDictionary(final ProvidedDictionaryDescription preferredDictionary);

    /**
     * Presents an error related to dictionary service.
     *
     * @param error the error
     */
    // TODO error should be a dedicated type
    void presentDictionaryError(final String error);
}
