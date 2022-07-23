package com.gitlab.super7ramp.crosswords.impl.solve;

import com.gitlab.super7ramp.crosswords.api.solve.SolveRequest;
import com.gitlab.super7ramp.crosswords.api.solve.SolverService;
import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionarySearch;
import com.gitlab.super7ramp.crosswords.spi.publisher.Publisher;
import com.gitlab.super7ramp.crosswords.spi.solver.CrosswordSolver;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

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
     * @param someSolvers             the solvers
     * @param someDictionaryProviders the dictionary providers
     * @throws IllegalArgumentException if solver collection is empty
     */
    public SolverServiceImpl(final Collection<CrosswordSolver> someSolvers,
                             final Collection<DictionaryProvider> someDictionaryProviders,
                             final Publisher aPublisher) {
        // Only one solver implementation for now
        solver = someSolvers.stream()
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Failed to " +
                                    "initialise solver service: No solver found."));
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
