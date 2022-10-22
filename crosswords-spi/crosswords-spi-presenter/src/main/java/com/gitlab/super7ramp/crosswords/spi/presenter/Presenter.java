package com.gitlab.super7ramp.crosswords.spi.presenter;

import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

import java.util.Collection;

/**
 * Required presentation services.
 */
// TODO create specific types so that Presenter SPI does not depend on Dictionary and Solver SPIs?
// TODO split Dictionary presenter from Solver presenter?
public interface Presenter {

    /**
     * Presents the result of a crossword solving request.
     *
     * @param result the solver result
     */
    void publishResult(final SolverResult result);

    /**
     * Presents an error from the crossword services.
     *
     * @param error the error
     */
    void publishError(final String error);

    /**
     * Presents the dictionary providers.
     *
     * @param providers the dictionary providers
     */
    void publishDictionaryProviders(final Collection<DictionaryProvider> providers);

    /**
     * Presents the dictionaries.
     *
     * @param filteredDictionaryProviders
     */
    void publishDictionaries(final Collection<DictionaryProvider> filteredDictionaryProviders);

    /**
     * Presents the entries of the given dictionary.
     *
     * @param dictionary the dictionary
     */
    void publishDictionaryEntries(final Dictionary dictionary);
}
