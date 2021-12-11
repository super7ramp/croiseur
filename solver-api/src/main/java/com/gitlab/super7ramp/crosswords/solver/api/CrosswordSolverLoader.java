package com.gitlab.super7ramp.crosswords.solver.api;

import com.gitlab.super7ramp.crosswords.solver.api.spi.CrosswordSolverProvider;

import java.util.NoSuchElementException;
import java.util.ServiceLoader;

/**
 * Load a {@link CrosswordSolver} implementation.
 */
public final class CrosswordSolverLoader {

    /**
     * Service implementation loader.
     */
    private static final ServiceLoader<CrosswordSolverProvider> LOADER = ServiceLoader.load(CrosswordSolverProvider.class);

    /**
     * Constructor.
     */
    private CrosswordSolverLoader() {
        // Nothing to do
    }

    /**
     * Return the default {@link CrosswordSolver}.
     *
     * @return the default {@link CrosswordSolver}
     */
    public static CrosswordSolver get() {
        return LOADER.findFirst().map(CrosswordSolverProvider::solver).orElseThrow(() ->
                new NoSuchElementException("No crossword solver implementation found"));
    }

}
