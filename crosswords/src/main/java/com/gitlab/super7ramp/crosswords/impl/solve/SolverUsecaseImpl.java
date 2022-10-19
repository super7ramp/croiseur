package com.gitlab.super7ramp.crosswords.impl.solve;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.crosswords.api.solver.SolveRequest;
import com.gitlab.super7ramp.crosswords.api.solver.SolverUsecase;
import com.gitlab.super7ramp.crosswords.impl.common.DictionarySelection;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.presenter.Presenter;
import com.gitlab.super7ramp.crosswords.spi.solver.CrosswordSolver;
import com.gitlab.super7ramp.crosswords.spi.solver.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public final class SolverUsecaseImpl implements SolverUsecase {

    /** The crossword solver. */
    private final CrosswordSolver solver;

    /** The dictionary loader. */
    private final Collection<DictionaryProvider> dictionaryProviders;

    /** The publisher. */
    private final Presenter presenter;

    /**
     * Constructor.
     *
     * @param someSolvers             the solvers
     * @param someDictionaryProviders the dictionary providers
     * @throws IllegalArgumentException if solver collection is empty
     */
    public SolverUsecaseImpl(final Collection<CrosswordSolver> someSolvers,
                             final Collection<DictionaryProvider> someDictionaryProviders,
                             final Presenter aPresenter) {
        // Only one solver implementation for now
        solver = someSolvers.stream()
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Failed to " +
                                    "initialise solver service: No solver found."));
        dictionaryProviders = new ArrayList<>(someDictionaryProviders);
        presenter = aPresenter;
    }

    @Override
    public void solve(final SolveRequest event) {

        final Optional<Dictionary> dictionary = retrieveDictionary(event);

        if (dictionary.isEmpty()) {
            presenter.publishError("Dictionary not found");
        } else {
            try {
                final SolverResult result = solver.solve(event.puzzle(), dictionary.get(),
                        event.progressListener());
                presenter.publishResult(result);
            } catch (final InterruptedException e) {
                presenter.publishError(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Retrieves the dictionary from request parameters.
     *
     * @param event the request
     * @return the dictionary, if any
     */
    private Optional<Dictionary> retrieveDictionary(final SolveRequest event) {

        // Create a DictionarySelection from the received event
        final Collection<DictionaryIdentifier> dictionaries = event.dictionaries();
        final DictionarySelection selection;
        if (dictionaries.isEmpty()) {
            // As per SolveRequest spec, no given dictionary means default dictionary
            selection = DictionarySelection.byDefault();
        } else {
            selection = dictionaries.stream()
                                    .map(DictionarySelection::byId)
                                    .reduce(DictionarySelection.none(), DictionarySelection::or);
        }

        // Retrieve all selected dictionaries
        final Collection<Dictionary> selectedDictionaries = selection
                .apply(dictionaryProviders)
                .stream()
                .flatMap(dictionaryProvider -> dictionaryProvider.get().stream())
                .map(this::toSolverDictionary)
                .toList();

        // At least one dictionary is necessary for solving
        if (selectedDictionaries.isEmpty()) {
            return Optional.empty();
        }

        // Return a composite of all selected dictionaries
        return Optional.of(new CompositeSolverDictionary(selectedDictionaries));

    }

    /**
     * Converts a dictionary from dictionary SPI to dictionary of solver SPI.
     *
     * @param dictionary the dictionary from dictionary SPI
     * @return the dictionary of solver SPI
     */
    private Dictionary toSolverDictionary(com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary dictionary) {
        return dictionary::lookup;
    }
}
