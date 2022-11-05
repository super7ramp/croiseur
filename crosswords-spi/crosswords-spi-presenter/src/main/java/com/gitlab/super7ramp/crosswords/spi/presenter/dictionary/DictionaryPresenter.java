package com.gitlab.super7ramp.crosswords.spi.presenter.dictionary;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryDescription;
import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryProviderDescription;

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
     * Note: The returned type is a map whose keys are {@link DictionaryProviderDescription}s.
     * This extra information is given to allow implementations to present dictionary provider
     * information alongside dictionaries, if they wish to do so.
     *
     * @param dictionaries the dictionaries
     */
    void presentDictionaries(final Map<DictionaryProviderDescription, Collection<?
            extends DictionaryDescription>> dictionaries);

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
     *
     * @param preferredDictionary the preferred dictionary, if any; {@code null} otherwise
     */
    void presentPreferredDictionary(final DictionaryDescription preferredDictionary);
}
