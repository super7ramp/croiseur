package com.gitlab.super7ramp.crosswords.spi.presenter;

import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryDescription;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProviderDescription;

import java.util.Collection;
import java.util.Map;

/**
 * Dictionary-related presentation services.
 */
// TODO create specific types so that Presenter SPI does not depend on Dictionary SPI?
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
     * Presents the entries of the given dictionary.
     *
     * @param dictionary the dictionary
     */
    void presentDictionaryEntries(final Dictionary dictionary);

    /**
     * Presents the preferred dictionary.
     *
     * @param preferredDictionary the preferred dictionary, if any; {@code null} otherwise
     */
    void presentPreferredDictionary(final DictionaryDescription preferredDictionary);
}
