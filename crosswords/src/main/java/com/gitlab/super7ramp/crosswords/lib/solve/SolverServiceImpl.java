package com.gitlab.super7ramp.crosswords.lib.solve;

import com.gitlab.super7ramp.crosswords.api.Publisher;
import com.gitlab.super7ramp.crosswords.api.solve.SolveRequest;
import com.gitlab.super7ramp.crosswords.api.solve.SolverService;
import com.gitlab.super7ramp.crosswords.dictionary.api.Dictionary;
import com.gitlab.super7ramp.crosswords.dictionary.api.DictionaryLoader;
import com.gitlab.super7ramp.crosswords.dictionary.spi.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.solver.api.CrosswordSolver;
import com.gitlab.super7ramp.crosswords.solver.api.CrosswordSolverLoader;
import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;

import java.util.Optional;
import java.util.function.Predicate;

public final class SolverServiceImpl implements SolverService {

    /** The crossword solver. */
    private final CrosswordSolver solver;

    /** The dictionary loader. */
    private final DictionaryLoader dictionaryLoader;

    /** The publisher. */
    private final Publisher publisher;

    /**
     * Constructor.
     *
     * @param solverLoader      the solver loader
     * @param aDictionaryLoader the dictionary loader
     */
    public SolverServiceImpl(final CrosswordSolverLoader solverLoader,
                             final DictionaryLoader aDictionaryLoader,
                             final Publisher aPublisher) {
        solver = solverLoader.get();
        dictionaryLoader = aDictionaryLoader;
        publisher = aPublisher;
    }

    @Override
    public void solve(final SolveRequest event) {

        final Optional<Dictionary> dictionary = retrieveDictionary(event);

        if (dictionary.isEmpty()) {
            publisher.publishError("Dictionary not found");
        } else {
            try {
                final SolverResult result = solver.solve(event.puzzle(), dictionary.get()::lookup);
                publisher.publishResult(result);
            } catch (final InterruptedException e) {
                publisher.publishError(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Retrieve dictionary from request parameters.
     *
     * @param event the request
     * @return the dictionary, if any
     */
    private Optional<Dictionary> retrieveDictionary(final SolveRequest event) {
        final Predicate<DictionaryProvider> providerFilter = DictionaryLoader.Search.includeAll();
        final Predicate<Dictionary> dictionaryFilter =
                event.dictionaryId().map(DictionaryLoader.Search::byName)
                     .orElseGet(DictionaryLoader.Search::includeAll);

        return dictionaryLoader.getFirst(providerFilter, dictionaryFilter);
    }
}
