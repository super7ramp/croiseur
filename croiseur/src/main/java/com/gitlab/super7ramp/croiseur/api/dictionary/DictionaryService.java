/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.api.dictionary;

/**
 * The dictionary service.
 * <p>
 * Mainly queries on the available dictionaries. Results of the requests are forwarded to the
 * {@link com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryPresenter
 * DictionaryPresenter}.
 */
public interface DictionaryService {

    /**
     * Lists available dictionary providers.
     *
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryPresenter#presentDictionaryProviders
     * DictionaryPresenter#presentDictionaryProviders
     */
    void listProviders();

    /**
     * Lists available dictionaries.
     *
     * @param request the request
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryPresenter#presentDictionaries
     * DictionaryPresenter#presentDictionaries
     */
    void listDictionaries(final ListDictionariesRequest request);

    /**
     * Lists entries for a dictionary.
     *
     * @param request the request
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryPresenter#presentDictionaryEntries
     * DictionaryPresenter#presentDictionaryEntries
     */
    void listEntries(final ListDictionaryEntriesRequest request);

    /**
     * Searches entries inside a dictionary.
     *
     * @param request the request
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryPresenter#presentDictionarySearchResult
     * DictionaryPresenter#presentDictionarySearchResult
     */
    void searchEntries(final SearchDictionaryEntriesRequest request);

    /**
     * Gets the default dictionary.
     * <p>
     * The criteria used to compare dictionaries are, by order of preference:
     * <ul>
     *     <li>Locale: Dictionary matching system's locale (language + country) is preferred over
     *     one which doesn't;</li>
     *     <li>Language: Dictionary matching system's language is preferred over one which doesn't;
     *     </li>
     *     <li>Fallback language: Dictionary matching English language is preferred over one which
     *     doesn't;</li>
     *     <li>Provider: Dictionary provided by "Local XML Provider" is preferred over one of an
     *     other provider;</li>
     *     <li>Name: Dictionary whose dictionary identifier string representation is smaller (in
     *     lexicographical sense) is preferred. (This is not a relevant criterion, it is used to
     *     guarantee the dictionary list can be completely sorted, assuming that identifiers are
     *     unique.)</li>
     * </ul>
     *
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryPresenter#presentDefaultDictionary
     * DictionaryPresenter#presentDefaultDictionary
     */
    void getDefaultDictionary();
}
