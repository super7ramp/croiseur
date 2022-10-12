package com.gitlab.super7ramp.crosswords.impl;

import com.gitlab.super7ramp.crosswords.api.CrosswordService;
import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryUsecase;
import com.gitlab.super7ramp.crosswords.api.solver.SolverUsecase;
import com.gitlab.super7ramp.crosswords.impl.dictionary.DictionaryUsecaseImpl;
import com.gitlab.super7ramp.crosswords.impl.solve.SolverUsecaseImpl;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.presenter.Presenter;
import com.gitlab.super7ramp.crosswords.spi.solver.CrosswordSolver;

import java.util.Collection;

/**
 * Implementation of {@link CrosswordService}.
 */
public final class CrosswordServiceImpl implements CrosswordService {

    /** Dictionary service. */
    private final DictionaryUsecase dictionaryUsecase;

    /** Solver service. */
    private final SolverUsecase solverUsecase;

    /**
     * Constructor.
     *
     * @param solvers             the solvers
     * @param dictionaryProviders the dictionary providers
     * @param presenter           the publisher
     */
    public CrosswordServiceImpl(final Collection<CrosswordSolver> solvers,
                                final Collection<DictionaryProvider> dictionaryProviders,
                                final Presenter presenter) {
        solverUsecase = new SolverUsecaseImpl(solvers, dictionaryProviders, presenter);
        dictionaryUsecase = new DictionaryUsecaseImpl(dictionaryProviders, presenter);
    }

    @Override
    public DictionaryUsecase dictionaryService() {
        return dictionaryUsecase;
    }

    @Override
    public SolverUsecase solverService() {
        return solverUsecase;
    }
}
