package com.gitlab.super7ramp.crosswords.spi.publisher;

import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

import java.util.Collection;

/**
 * Required publishing services.
 * TODO move into its own module i.e. publisher-api
 */
// TODO create specific types so that Publisher SPI does not depend on Dictionary and Solver SPIs
public interface Publisher {

    void publishResult(final SolverResult result);

    void publishError(final String error);

    void publishDictionaryProviders(final Collection<DictionaryProvider> providers);

    void publishDictionaries(final Collection<DictionaryProvider> filteredDictionaryProviders);

    void publishDictionaryEntries(final Dictionary dictionary);
}
