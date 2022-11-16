package com.gitlab.super7ramp.crosswords.spi.presenter.dictionary;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryDescription;
import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.crosswords.common.dictionary.ProvidedDictionaryDescription;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
     * Presents the entries of a dictionary.
     * <p>
     * Entries are sorted alphabetically.
     *
     * @param entries the dictionary entries
     */
    void presentDictionaryEntries(final List<String> entries);

    /**
     * Presents the preferred dictionary.
     * <p>
     * Typically, this is the first dictionary returned by {@link #presentDictionaries(List)}.
     *
     * @param preferredDictionary the preferred dictionary, if any; {@code null} otherwise
     */
    void presentPreferredDictionary(final ProvidedDictionaryDescription preferredDictionary);
}
