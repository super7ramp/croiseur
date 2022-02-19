package com.gitlab.super7ramp.crosswords.api;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.crosswords.api.solve.SolverService;
import com.gitlab.super7ramp.crosswords.dictionary.api.DictionaryLoader;
import com.gitlab.super7ramp.crosswords.lib.CrosswordServiceImpl;
import com.gitlab.super7ramp.crosswords.solver.api.CrosswordSolverLoader;

/**
 * Crossword services.
 */
public interface CrosswordService {

    /**
     * Create a new instance of {@link CrosswordService}.
     *
     * @param solverLoader     the solver service loader
     * @param dictionaryLoader the dictionary service loader
     * @param publisher        the publisher service
     * @return a new instance of {@link CrosswordService}
     */
    static CrosswordService create(final CrosswordSolverLoader solverLoader,
                                   final DictionaryLoader dictionaryLoader,
                                   final Publisher publisher) {
        return new CrosswordServiceImpl(solverLoader, dictionaryLoader, publisher);
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
