package com.gitlab.super7ramp.crosswords.cli.core.lib;

import com.gitlab.super7ramp.crosswords.cli.core.api.CrosswordService;
import com.gitlab.super7ramp.crosswords.cli.core.api.Publisher;
import com.gitlab.super7ramp.crosswords.cli.core.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.crosswords.cli.core.api.solve.SolverService;
import com.gitlab.super7ramp.crosswords.cli.core.lib.dictionary.DictionaryServiceImpl;
import com.gitlab.super7ramp.crosswords.cli.core.lib.solve.SolverServiceImpl;
import com.gitlab.super7ramp.crosswords.dictionary.api.DictionaryLoader;
import com.gitlab.super7ramp.crosswords.solver.api.CrosswordSolverLoader;

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
