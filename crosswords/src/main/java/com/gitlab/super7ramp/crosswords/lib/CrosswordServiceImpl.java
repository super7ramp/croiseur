package com.gitlab.super7ramp.crosswords.lib;

import com.gitlab.super7ramp.crosswords.api.CrosswordService;
import com.gitlab.super7ramp.crosswords.api.Publisher;
import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.crosswords.api.solve.SolverService;
import com.gitlab.super7ramp.crosswords.dictionary.spi.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.lib.dictionary.DictionaryServiceImpl;
import com.gitlab.super7ramp.crosswords.lib.solve.SolverServiceImpl;
import com.gitlab.super7ramp.crosswords.solver.spi.CrosswordSolverProvider;

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
     * @param solverProviders     solver providers
     * @param dictionaryProviders dictionary providers
     * @param publisher           output stream
     */
    public CrosswordServiceImpl(final Collection<CrosswordSolverProvider> solverProviders,
                                final Collection<DictionaryProvider> dictionaryProviders,
                                final Publisher publisher) {
        solverService = new SolverServiceImpl(solverProviders, dictionaryProviders, publisher);
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
