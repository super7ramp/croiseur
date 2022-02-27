package com.gitlab.super7ramp.crosswords.lib;

import com.gitlab.super7ramp.crosswords.api.CrosswordService;
import com.gitlab.super7ramp.crosswords.api.Publisher;
import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.crosswords.api.solve.SolverService;
import com.gitlab.super7ramp.crosswords.dictionary.spi.DictionaryLoader;
import com.gitlab.super7ramp.crosswords.lib.dictionary.DictionaryServiceImpl;
import com.gitlab.super7ramp.crosswords.lib.solve.SolverServiceImpl;
import com.gitlab.super7ramp.crosswords.solver.spi.CrosswordSolverLoader;

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
     * @param solverLoader     solver loader
     * @param dictionaryLoader dictionary loader
     * @param publisher        output stream
     */
    public CrosswordServiceImpl(final CrosswordSolverLoader solverLoader,
                                final DictionaryLoader dictionaryLoader,
                                final Publisher publisher) {
        solverService = new SolverServiceImpl(solverLoader, dictionaryLoader, publisher);
        dictionaryService = new DictionaryServiceImpl(dictionaryLoader, publisher);
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
