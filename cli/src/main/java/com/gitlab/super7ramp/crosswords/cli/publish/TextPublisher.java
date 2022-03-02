package com.gitlab.super7ramp.crosswords.cli.publish;

import com.gitlab.super7ramp.crosswords.api.Publisher;
import com.gitlab.super7ramp.crosswords.dictionary.api.Dictionary;
import com.gitlab.super7ramp.crosswords.dictionary.spi.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;

import java.util.Collection;
import java.util.Iterator;

/**
 * Text implementation of {@link Publisher}.
 */
public final class TextPublisher implements Publisher {

    /** Providers output format. */
    private static final String PROVIDERS_FORMAT = "%-16s\t%-54s%n";

    /** List output format. */
    private static final String LIST_FORMAT = "%-16s\t%-16s\t%-16s%n";

    /**
     * Constructor.
     */
    public TextPublisher() {
        // Nothing to do
    }

    @Override
    public void publishResult(final SolverResult result) {
        System.out.println(result);
    }

    @Override
    public void publishError(final String error) {
        System.err.println(error);
    }

    @Override
    public void publishDictionaryProviders(final Collection<DictionaryProvider> providers) {

        System.out.printf(PROVIDERS_FORMAT, "Provider", "Description");
        System.out.printf(PROVIDERS_FORMAT, "--------", "-----------");

        providers.forEach(provider -> System.out.printf(PROVIDERS_FORMAT, provider.name(),
                provider.description()));
    }

    @Override
    public void publishDictionaries(final Collection<DictionaryProvider> filteredDictionaryProviders) {

        System.out.printf(LIST_FORMAT, "Provider", "Name", "Locale");
        System.out.printf(LIST_FORMAT, "--------", "----", "------");

        for (final DictionaryProvider dictionaryProvider : filteredDictionaryProviders) {
            dictionaryProvider.get()
                              .forEach(dictionary -> System.out.printf(LIST_FORMAT,
                                      dictionaryProvider.name(), dictionary.name(),
                                      dictionary.locale()
                                                .getDisplayName()));
        }
    }

    @Override
    public void publishDictionaryEntries(final Dictionary dictionary) {
        /*
         * As output may be very large, it is likely the output is going to be piped (grep,
         * head, ...). Checking error status on System.out allows detecting broken pipe and
         * fast exit.
         */
        final Iterator<String> wordIterator = dictionary.lookup(word -> true).iterator();
        while (wordIterator.hasNext() && !System.out.checkError()) {
            System.out.println(wordIterator.next());
        }
    }
}
