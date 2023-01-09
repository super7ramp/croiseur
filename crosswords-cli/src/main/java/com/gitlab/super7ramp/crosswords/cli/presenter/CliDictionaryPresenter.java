package com.gitlab.super7ramp.crosswords.cli.presenter;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryDescription;
import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.crosswords.common.dictionary.ProvidedDictionaryDescription;
import com.gitlab.super7ramp.crosswords.spi.presenter.dictionary.DictionaryContent;
import com.gitlab.super7ramp.crosswords.spi.presenter.dictionary.DictionaryPresenter;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * CLI implementation of {@link DictionaryPresenter}.
 */
final class CliDictionaryPresenter implements DictionaryPresenter {

    /** The translated strings. */
    private static final ResourceBundle L10N = ResourceBundle.getBundle("/l10n/DictionaryMessages");

    /** Providers output format. */
    private static final String PROVIDERS_FORMAT = "%-16s\t%-54s%n";

    /** List output format. */
    private static final String LIST_FORMAT = "%-16s\t%-48s\t%-16s%n";

    /** Show preferred dictionary format. */
    private static final String PREFERRED_DICTIONARY_FORMAT = L10N.getString("preferred.format");

    /**
     * Constructs an instance.
     */
    CliDictionaryPresenter() {
        // Nothing to do.
    }

    @Override
    public void presentDictionaryProviders(final Collection<DictionaryProviderDescription> providers) {

        System.out.printf(PROVIDERS_FORMAT, L10N.getString("provider"), L10N.getString(
                "description"));
        System.out.printf(PROVIDERS_FORMAT, "--------", "-----------");

        providers.forEach(provider -> System.out.printf(PROVIDERS_FORMAT, provider.name(),
                provider.description()));
    }

    @Override
    public void presentDictionaries(final List<ProvidedDictionaryDescription> dictionaries) {

        System.out.printf(LIST_FORMAT, L10N.getString("provider"), L10N.getString(
                "name"), L10N.getString("locale"));
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

    @Override
    public void presentDictionaryError(final String error) {
        System.err.println(error);
    }
}
