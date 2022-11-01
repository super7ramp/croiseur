package com.gitlab.super7ramp.crosswords.impl.solve;

import com.gitlab.super7ramp.crosswords.api.solver.SolveRequest;
import com.gitlab.super7ramp.crosswords.api.solver.SolverUsecase;
import com.gitlab.super7ramp.crosswords.common.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.presenter.Presenter;
import com.gitlab.super7ramp.crosswords.spi.solver.CrosswordSolver;
import com.gitlab.super7ramp.crosswords.spi.solver.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.solver.ProgressListener;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

import java.util.Collection;
import java.util.Optional;

public final class SolverUsecaseImpl implements SolverUsecase {

    /** The crossword solver. */
    private final CrosswordSolver solver;

    /** The dictionary loader. */
    private final DictionaryLoader dictionaries;

    /** The publisher. */
    private final Presenter presenter;

    private final ProgressListenerFactory progressListenerFactory;

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
        presenter = aPresenter;
        dictionaries = new DictionaryLoader(someDictionaryProviders);
        progressListenerFactory = new ProgressListenerFactory(presenter);
    }

    @Override
    public void solve(final SolveRequest event) {

        final Optional<Dictionary> optDictionary = dictionaries.load(event.dictionaries());

        if (optDictionary.isEmpty()) {
            presenter.presentError("Dictionary not found");
        } else {

            final PuzzleDefinition puzzle = event.puzzle();
            final Dictionary dictionary = optDictionary.get();
            final ProgressListener progressListener =
                    progressListenerFactory.from(event.progress());

            try {
                final SolverResult result = solver.solve(puzzle, dictionary, progressListener);
                presenter.presentResult(result);
            } catch (final InterruptedException e) {
                presenter.presentError(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

}
