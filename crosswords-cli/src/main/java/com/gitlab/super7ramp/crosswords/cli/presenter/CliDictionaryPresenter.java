package com.gitlab.super7ramp.crosswords.cli.presenter;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryDescription;
import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.crosswords.common.dictionary.ProvidedDictionaryDescription;
import com.gitlab.super7ramp.crosswords.spi.presenter.dictionary.DictionaryContent;
import com.gitlab.super7ramp.crosswords.spi.presenter.dictionary.DictionaryPresenter;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * CLI implementation of {@link DictionaryPresenter}.
 */
final class CliDictionaryPresenter implements DictionaryPresenter {

    /** Providers output format. */
    private static final String PROVIDERS_FORMAT = "%-16s\t%-54s%n";

    /** List output format. */
    private static final String LIST_FORMAT = "%-16s\t%-16s\t%-16s%n";

    /** Show preferred dictionary format. */
    private static final String PREFERRED_DICTIONARY_FORMAT = "%s, %s, provided by %s";

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
    public void presentDictionaries(final List<ProvidedDictionaryDescription> dictionaries) {

        System.out.printf(LIST_FORMAT, "Provider", "Name", "Locale");
        System.out.printf(LIST_FORMAT, "--------", "----", "------");

        for (final ProvidedDictionaryDescription providedDictionary : dictionaries) {
            final DictionaryProviderDescription provider = providedDictionary.provider();
            final DictionaryDescription dictionary = providedDictionary.dictionary();
            System.out.printf(LIST_FORMAT, provider.name(), dictionary.name(), dictionary.locale()
                                                                                         .getDisplayName());
        }
    }

    @Override
    public void presentDictionaryEntries(final DictionaryContent content) {
        /*
         * As output may be very large, it is likely the output is going to be piped (grep,
         * head, ...). Checking error status on System.out allows detecting broken pipe and
         * fast exit.
         */
        final Iterator<String> wordIterator = content.words().iterator();
        while (wordIterator.hasNext() && !System.out.checkError()) {
            System.out.println(wordIterator.next());
        }
    }

    @Override
    public void presentPreferredDictionary(final ProvidedDictionaryDescription preferredDictionary) {
        final DictionaryProviderDescription provider = preferredDictionary.provider();
        final DictionaryDescription dictionary = preferredDictionary.dictionary();
        System.out.printf(PREFERRED_DICTIONARY_FORMAT, dictionary.name(),
                dictionary.locale().getDisplayName(), provider.name());
    }
}
