package com.gitlab.super7ramp.crosswords.solver.api;

import com.gitlab.super7ramp.crosswords.solver.spi.CrosswordSolverProvider;

import java.util.NoSuchElementException;
import java.util.ServiceLoader;

/**
 * Load a {@link CrosswordSolver} implementation.
 */
public final class CrosswordSolverLoader {

    /** Service implementation loader. */
    private final ServiceLoader<CrosswordSolverProvider> loader;

    /**
     * Constructor.
     *
     * @param aLoader the actual loader
     */
    public CrosswordSolverLoader(final ServiceLoader<CrosswordSolverProvider> aLoader) {
        loader = aLoader;
    }

    /**
     * Return the default {@link CrosswordSolver}.
     *
     * @return the default {@link CrosswordSolver}
     */
    public CrosswordSolver get() {
        return loader.findFirst()
                     .map(CrosswordSolverProvider::solver)
                     .orElseThrow(() -> new NoSuchElementException("No crossword solver " +
                             "implementation found"));
    }

}
