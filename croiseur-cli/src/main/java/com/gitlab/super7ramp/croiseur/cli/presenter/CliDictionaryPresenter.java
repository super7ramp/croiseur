/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.presenter;

import com.gitlab.super7ramp.croiseur.cli.l10n.ResourceBundles;
import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryProviderDetails;
import com.gitlab.super7ramp.croiseur.common.dictionary.ProvidedDictionaryDetails;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryContent;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryPresenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionarySearchResult;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static com.gitlab.super7ramp.croiseur.cli.presenter.CliPresenterUtil.lineOf;

/**
 * CLI implementation of {@link DictionaryPresenter}.
 */
final class CliDictionaryPresenter implements DictionaryPresenter {

    /** Providers output format. */
    private static final String PROVIDERS_FORMAT = "%-16s\t%-54s%n";

    /** List output format. */
    private static final String LIST_FORMAT = "%-16s\t%-48s\t%-16s%n";

    /**
     * Constructs an instance.
     */
    CliDictionaryPresenter() {
        // Nothing to do.
    }

    /**
     * Returns the localised message with given key.
     *
     * @param key the message key
     * @return the localised message
     */
    private static String $(final String key) {
        return ResourceBundles.$("presenter.dictionary." + key);
    }

    @Override
    public void presentDictionaryProviders(
            final Collection<DictionaryProviderDetails> providers) {
        final String providerHeader = $("provider");
        final String descriptionHeader = $("description");

        System.out.printf(PROVIDERS_FORMAT, providerHeader, descriptionHeader);
        System.out.printf(PROVIDERS_FORMAT, lineOf(providerHeader.length()),
                          lineOf(descriptionHeader.length()));

        providers.forEach(provider -> System.out.printf(PROVIDERS_FORMAT, provider.name(),
                                                        provider.description()));
    }

    @Override
    public void presentDictionaries(final List<ProvidedDictionaryDetails> dictionaries) {
        final String providerHeader = $("provider");
        final String nameHeader = $("name");
        final String localeHeader = $("locale");

        System.out.printf(LIST_FORMAT, providerHeader, nameHeader, localeHeader);
        System.out.printf(LIST_FORMAT, lineOf(providerHeader.length()), lineOf(nameHeader.length()),
                          lineOf(localeHeader.length()));

        for (final ProvidedDictionaryDetails providedDictionary : dictionaries) {
            System.out.printf(LIST_FORMAT, providedDictionary.providerName(),
                              providedDictionary.dictionaryName(),
                              providedDictionary.dictionaryLocale().getDisplayName());
        }
    }

    @Override
    public void presentDictionaryEntries(final DictionaryContent content) {
        printWords(content.words());
    }

    @Override
    public void presentDictionarySearchResult(final DictionarySearchResult searchResult) {
        printWords(searchResult.words());
    }

    @Override
    public void presentDefaultDictionary(final ProvidedDictionaryDetails defaultDictionary) {
        System.out.printf($("preferred.format"), defaultDictionary.dictionaryName(),
                          defaultDictionary.dictionaryLocale().getDisplayName(),
                          defaultDictionary.providerName());
    }

    @Override
    public void presentDictionaryError(final String error) {
        System.err.println(error);
    }

    /**
     * Prints the given words to standard output.
     *
     * @param words the words to print
     */
    private void printWords(final Iterable<String> words) {
        /*
         * As output may be very large, it is likely the output is going to be piped (grep,
         * head, ...). Checking error status on System.out allows detecting broken pipe and
         * fast exit.
         */
        final Iterator<String> wordIterator = words.iterator();
        while (wordIterator.hasNext() && !System.out.checkError()) {
            System.out.println(wordIterator.next());
        }
    }
}
