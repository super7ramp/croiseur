package com.gitlab.super7ramp.crosswords.lib.solve;

import com.gitlab.super7ramp.crosswords.api.Publisher;
import com.gitlab.super7ramp.crosswords.api.solve.SolveRequest;
import com.gitlab.super7ramp.crosswords.api.solve.SolverService;
import com.gitlab.super7ramp.crosswords.dictionary.api.Dictionary;
import com.gitlab.super7ramp.crosswords.dictionary.spi.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.dictionary.spi.DictionarySearch;
import com.gitlab.super7ramp.crosswords.solver.api.CrosswordSolver;
import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;
import com.gitlab.super7ramp.crosswords.solver.spi.CrosswordSolverProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public final class SolverServiceImpl implements SolverService {

    /** The crossword solver. */
    private final CrosswordSolver solver;

    /** The dictionary loader. */
    private final Collection<DictionaryProvider> dictionaryProviders;

    /** The publisher. */
    private final Publisher publisher;

    /**
     * Constructor.
     *
     * @param someSolverProviders     the solver providers
     * @param someDictionaryProviders the dictionary providers
     */
    public SolverServiceImpl(final Collection<CrosswordSolverProvider> someSolverProviders,
                             final Collection<DictionaryProvider> someDictionaryProviders,
                             final Publisher aPublisher) {
        // Only one solver implementation for now
        solver = someSolverProviders.stream()
                                    .map(CrosswordSolverProvider::solver)
                                    .findFirst()
                                    .orElseThrow();
        dictionaryProviders = new ArrayList<>(someDictionaryProviders);
        publisher = aPublisher;
    }

    @Override
    public void solve(final SolveRequest event) {

        final Optional<Dictionary> dictionary = retrieveDictionary(event);

        if (dictionary.isEmpty()) {
            publisher.publishError("Dictionary not found");
        } else {
            try {
                final SolverResult result = solver.solve(event.puzzle(), dictionary.get()::lookup
                        , event.progressListener());
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
        // TODO filter on provider as well
        return DictionarySearch.byOptionalName(event.dictionaryId())
                               .apply(dictionaryProviders)
                               .stream()
                               .map(DictionaryProvider::getFirst)
                               .flatMap(Optional::stream)
                               .findFirst();
    }
}
