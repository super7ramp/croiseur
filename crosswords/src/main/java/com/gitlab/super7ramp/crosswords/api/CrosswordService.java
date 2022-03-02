package com.gitlab.super7ramp.crosswords.api;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.crosswords.api.solve.SolverService;
import com.gitlab.super7ramp.crosswords.dictionary.spi.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.lib.CrosswordServiceImpl;
import com.gitlab.super7ramp.crosswords.solver.spi.CrosswordSolverProvider;

import java.util.Collection;

/**
 * Crossword services.
 */
public interface CrosswordService {

    /**
     * Create a new instance of {@link CrosswordService}.
     *
     * @param solverProviders     the solver service providers
     * @param dictionaryProviders the dictionary service providers
     * @param publisher           the publisher service
     * @return a new instance of {@link CrosswordService}
     */
    static CrosswordService create(final Collection<CrosswordSolverProvider> solverProviders,
                                   final Collection<DictionaryProvider> dictionaryProviders,
                                   final Publisher publisher) {
        return new CrosswordServiceImpl(solverProviders, dictionaryProviders, publisher);
    }

    /**
     * @return the dictionary service
     */
    DictionaryService dictionaryService();

    /**
     * @return the solver service
     */
    SolverService solverService();

}
