package com.gitlab.super7ramp.crosswords.cli.presenter;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryDescription;
import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.crosswords.spi.presenter.dictionary.DictionaryPresenter;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * CLI implementation of {@link DictionaryPresenter}.
 */
final class CliDictionaryPresenter implements DictionaryPresenter {

    /** Providers output format. */
    private static final String PROVIDERS_FORMAT = "%-16s\t%-54s%n";

    /** List output format. */
    private static final String LIST_FORMAT = "%-16s\t%-16s\t%-16s%n";

    /**
     * Constructs an instance.
     */
    CliDictionaryPresenter() {
        // Nothing to do.
    }

    @Override
    public void presentDictionaryProviders(final Collection<DictionaryProviderDescription> providers) {

        System.out.printf(PROVIDERS_FORMAT, "Provider", "Description");
        System.out.printf(PROVIDERS_FORMAT, "--------", "-----------");

        providers.forEach(provider -> System.out.printf(PROVIDERS_FORMAT, provider.name(),
                provider.description()));
    }

    @Override
    public void presentDictionaries(final Map<DictionaryProviderDescription, Collection<?
            extends DictionaryDescription>> dictionariesPerProviders) {

        System.out.printf(LIST_FORMAT, "Provider", "Name", "Locale");
        System.out.printf(LIST_FORMAT, "--------", "----", "------");

        for (final Map.Entry<DictionaryProviderDescription, Collection<?
                extends DictionaryDescription>> entry :
                dictionariesPerProviders.entrySet()) {

            final DictionaryProviderDescription provider = entry.getKey();
            final Collection<? extends DictionaryDescription> dictionaries = entry.getValue();
            dictionaries.forEach(dictionary -> System.out.printf(LIST_FORMAT, provider.name(),
                    dictionary.name(), dictionary.locale()
                                                 .getDisplayName()));
        }
    }

    @Override
    public void presentDictionaryEntries(final List<String> entries) {
        /*
         * As output may be very large, it is likely the output is going to be piped (grep,
         * head, ...). Checking error status on System.out allows detecting broken pipe and
         * fast exit.
         */
        final Iterator<String> wordIterator = entries.iterator();
        while (wordIterator.hasNext() && !System.out.checkError()) {
            System.out.println(wordIterator.next());
        }
    }

    @Override
    public void presentPreferredDictionary(final DictionaryDescription preferredDictionary) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
