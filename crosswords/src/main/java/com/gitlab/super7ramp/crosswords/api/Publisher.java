package com.gitlab.super7ramp.crosswords.api;

import com.gitlab.super7ramp.crosswords.dictionary.api.Dictionary;
import com.gitlab.super7ramp.crosswords.dictionary.spi.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;

import java.util.Collection;
import java.util.Map;

/**
 * Required publishing services.
 */
public interface Publisher {

    void publishResult(final SolverResult result);

    void publishError(final String error);

    void publishDictionaryProviders(final Collection<DictionaryProvider> providers);

    void publishDictionaries(final Map<DictionaryProvider, Collection<Dictionary>> dictionariesByProvider);

    void publishDictionaryEntries(final Dictionary dictionary);
}
