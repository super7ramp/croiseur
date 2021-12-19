package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.CrosswordSolver;
import com.gitlab.super7ramp.crosswords.solver.spi.CrosswordSolverProvider;

/**
 * Implementation of {@link CrosswordSolverProvider}.
 */
public final class CrosswordSolverProviderImpl implements CrosswordSolverProvider {

    /**
     * Constructor.
     */
    public CrosswordSolverProviderImpl() {
        // Nothing to do.
    }

    @Override
    public CrosswordSolver solver() {
        return new CrosswordSolverImpl();
    }
}
