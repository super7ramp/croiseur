package com.gitlab.super7ramp.crosswords.impl.solver;

import com.gitlab.super7ramp.crosswords.api.solver.SolveRequest;
import com.gitlab.super7ramp.crosswords.api.solver.SolverService;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverPresenter;
import com.gitlab.super7ramp.crosswords.spi.solver.CrosswordSolver;

import java.util.Collection;

/**
 * Implementation of {@link SolverService}.
 */
public final class SolverServiceImpl implements SolverService {

    /** The 'list-solver' usecase. */
    private final ListSolversUsecase listSolversUsecase;

    /** The 'solve' usecase. */
    private final SolveUsecase solveUsecase;

    /**
     * Constructs an instance.
     *
     * @param solversArg             the solvers
     * @param dictionaryProvidersArg the dictionary providers
     * @param presenterArg           the solver presenter
     * @throws IllegalArgumentException if solver collection is empty
     */
    public SolverServiceImpl(final Collection<CrosswordSolver> solversArg,
                             final Collection<DictionaryProvider> dictionaryProvidersArg,
                             final SolverPresenter presenterArg) {

        listSolversUsecase = new ListSolversUsecase(solversArg, presenterArg);
        solveUsecase = new SolveUsecase(solversArg, dictionaryProvidersArg, presenterArg);
    }

    @Override
    public void listProviders() {
        listSolversUsecase.process();
    }

    @Override
    public void solve(final SolveRequest event) {
        solveUsecase.process(event);
    }

}