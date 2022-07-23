package com.gitlab.super7ramp.crosswords.impl;

import com.gitlab.super7ramp.crosswords.api.CrosswordService;
import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.crosswords.api.solve.SolverService;
import com.gitlab.super7ramp.crosswords.impl.dictionary.DictionaryServiceImpl;
import com.gitlab.super7ramp.crosswords.impl.solve.SolverServiceImpl;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.publisher.Publisher;
import com.gitlab.super7ramp.crosswords.spi.solver.CrosswordSolver;

import java.util.Collection;

/**
 * Implementation of {@link CrosswordService}.
 */
public final class CrosswordServiceImpl implements CrosswordService {

    /** Dictionary service. */
    private final DictionaryService dictionaryService;

    /** Solver service. */
    private final SolverService solverService;

    /**
     * Constructor.
     *
     * @param solvers             the solvers
     * @param dictionaryProviders the dictionary providers
     * @param publisher           the publisher
     */
    public CrosswordServiceImpl(final Collection<CrosswordSolver> solvers,
                                final Collection<DictionaryProvider> dictionaryProviders,
                                final Publisher publisher) {
        solverService = new SolverServiceImpl(solvers, dictionaryProviders, publisher);
        dictionaryService = new DictionaryServiceImpl(dictionaryProviders, publisher);
    }

    @Override
    public DictionaryService dictionaryService() {
        return dictionaryService;
    }

    @Override
    public SolverService solverService() {
        return solverService;
    }
}
