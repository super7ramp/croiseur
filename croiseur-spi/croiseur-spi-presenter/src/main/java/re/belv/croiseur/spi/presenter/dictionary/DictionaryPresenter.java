/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.spi.presenter.dictionary;

import java.util.Collection;
import java.util.List;
import re.belv.croiseur.common.dictionary.DictionaryProviderDetails;
import re.belv.croiseur.common.dictionary.ProvidedDictionaryDetails;

/** Dictionary-related presentation services. */
public interface DictionaryPresenter {

    /**
     * Presents the dictionary providers.
     *
     * @param providers the dictionary providers
     */
    void presentDictionaryProviders(final Collection<DictionaryProviderDetails> providers);

    /**
     * Presents the dictionaries.
     *
     * <p>The returned dictionaries are sorted by order of preference.
     *
     * @param dictionaries the dictionaries
     */
    void presentDictionaries(final List<ProvidedDictionaryDetails> dictionaries);

    /**
     * Presents the content of a dictionary.
     *
     * <p>Dictionary word list is given as read from the dictionary: It may or may not be sorted. Presenter should sort
     * it when sort order is important.
     *
     * @param content the dictionary content
     */
    void presentDictionaryEntries(final DictionaryContent content);

    /**
     * Presents the result of search.
     *
     * @param searchResult the search result
     */
    void presentDictionarySearchResult(final DictionarySearchResult searchResult);

    /**
     * Presents the default dictionary.
     *
     * <p>Typically, this is the first dictionary returned by {@link #presentDictionaries(List)}.
     *
     * @param defaultDictionary the default dictionary, if any; {@code null} otherwise
     */
    void presentDefaultDictionary(final ProvidedDictionaryDetails defaultDictionary);

    /**
     * Presents an error related to dictionary service.
     *
     * @param error the error
     */
    // TODO error should be a dedicated type
    void presentDictionaryError(final String error);
}
