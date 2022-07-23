package com.gitlab.super7ramp.crosswords.api;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.crosswords.api.solve.SolverService;
import com.gitlab.super7ramp.crosswords.impl.CrosswordServiceImpl;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.publisher.Publisher;
import com.gitlab.super7ramp.crosswords.spi.solver.CrosswordSolver;

import java.util.Collection;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * Crossword services.
 */
public interface CrosswordService {

    /**
     * Creates a new instance of {@link CrosswordService}.
     * <p>
     * Required services are explicitly passed as arguments.
     *
     * @param solvers             the solver services
     * @param dictionaryProviders the dictionary service providers
     * @param publisher           the publisher service
     * @return a new instance of {@link CrosswordService}
     */
    static CrosswordService create(final Collection<CrosswordSolver> solvers,
                                   final Collection<DictionaryProvider> dictionaryProviders,
                                   final Publisher publisher) {
        return new CrosswordServiceImpl(solvers, dictionaryProviders, publisher);
    }

    /**
     * Creates a new instance of {@link CrosswordService}.
     * <p>
     * Required services are automatically loaded using {@link ServiceLoader}.
     *
     * @return a new instance of {@link CrosswordService}
     * @throws IllegalStateException if some required services cannot be found
     */
    static CrosswordService create() {
        final Collection<DictionaryProvider> dictionaryProviders =
                ServiceLoader.load(DictionaryProvider.class).stream()
                             .map(Supplier::get)
                             .toList();
        final Collection<CrosswordSolver> solvers =
                ServiceLoader.load(CrosswordSolver.class)
                             .stream()
                             .map(Supplier::get)
                             .toList();
        final Publisher publisher =
                ServiceLoader.load(Publisher.class)
                             .findFirst()
                             .orElseThrow(() -> new IllegalStateException(
                                     "Failed to instantiate crosswords service: No publisher " +
                                             "found"));
        return create(solvers, dictionaryProviders, publisher);
    }

    /**
     * Returns the dictionary service.
     *
     * @return the dictionary service
     */
    DictionaryService dictionaryService();

    /**
     * Returns the solver service.
     *
     * @return the solver service
     */
    SolverService solverService();

}
