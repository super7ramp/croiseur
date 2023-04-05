/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.presenter;

import com.gitlab.super7ramp.croiseur.cli.l10n.ResourceBundles;
import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryDescription;
import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.croiseur.common.dictionary.ProvidedDictionaryDescription;
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
            final Collection<DictionaryProviderDescription> providers) {
        final String providerHeader = $("provider");
        final String descriptionHeader = $("description");

        System.out.printf(PROVIDERS_FORMAT, providerHeader, descriptionHeader);
        System.out.printf(PROVIDERS_FORMAT, lineOf(providerHeader.length()),
                lineOf(descriptionHeader.length()));

        providers.forEach(provider -> System.out.printf(PROVIDERS_FORMAT, provider.name(),
                provider.description()));
    }

    @Override
    public void presentDictionaries(final List<ProvidedDictionaryDescription> dictionaries) {
        final String providerHeader = $("provider");
        final String nameHeader = $("name");
        final String localeHeader = $("locale");

        System.out.printf(LIST_FORMAT, providerHeader, nameHeader, localeHeader);
        System.out.printf(LIST_FORMAT, lineOf(providerHeader.length()),
                lineOf(nameHeader.length()), lineOf(localeHeader.length()));

        for (final ProvidedDictionaryDescription providedDictionary : dictionaries) {
            final DictionaryProviderDescription provider = providedDictionary.provider();
            final DictionaryDescription dictionary = providedDictionary.dictionary();
            System.out.printf(LIST_FORMAT, provider.name(), dictionary.name(), dictionary.locale()
                    .getDisplayName());
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
    public void presentPreferredDictionary(
            final ProvidedDictionaryDescription preferredDictionary) {
        final DictionaryProviderDescription provider = preferredDictionary.provider();
        final DictionaryDescription dictionary = preferredDictionary.dictionary();
        System.out.printf($("preferred.format"), dictionary.name(), dictionary.locale()
                .getDisplayName(), provider.name());
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
