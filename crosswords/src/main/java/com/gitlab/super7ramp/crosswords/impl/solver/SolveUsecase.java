package com.gitlab.super7ramp.crosswords.impl.solver;

import com.gitlab.super7ramp.crosswords.api.solver.SolveRequest;
import com.gitlab.super7ramp.crosswords.common.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverPresenter;
import com.gitlab.super7ramp.crosswords.spi.solver.CrosswordSolver;
import com.gitlab.super7ramp.crosswords.spi.solver.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.solver.ProgressListener;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

/**
 * Solve usecase.
 */
final class SolveUsecase {

    /** The crossword solvers indexed by names. */
    private final Map<String, CrosswordSolver> solvers;

    /** The dictionary loader. */
    private final DictionaryLoader dictionaryLoader;

    /** The publisher. */
    private final SolverPresenter presenter;

    /** A factory to create {@link ProgressListener}s from {@link SolveRequest}s. */
    private final ProgressListenerFactory progressListenerFactory;

    /**
     * Constructs an instance.
     *
     * @param solversArg             the solvers
     * @param dictionaryProvidersArg the dictionary providers
     * @param presenterArg           the presenter
     */
    SolveUsecase(final Collection<CrosswordSolver> solversArg,
                 final Collection<DictionaryProvider> dictionaryProvidersArg,
                 final SolverPresenter presenterArg) {
        solvers = collectSolversMap(solversArg);
        presenter = presenterArg;
        dictionaryLoader = new DictionaryLoader(dictionaryProvidersArg);
        progressListenerFactory = new ProgressListenerFactory(presenter);
    }

    /**
     * Creates a map associating the given solvers to their names.
     *
     * @param solvers the solvers
     * @return a map associating the given solvers to their names
     */
    private static Map<String, CrosswordSolver> collectSolversMap(final Collection<CrosswordSolver> solvers) {
        if (solvers.isEmpty()) {
            throw new IllegalArgumentException("Failed to initialise solver service: No solver " +
                    "found.");
        }
        return solvers.stream().collect(toMap(CrosswordSolver::name, Function.identity()));
    }

    /**
     * Processes the given {@link SolveRequest}.
     *
     * @param event the solve request to process
     */
    void process(final SolveRequest event) {

        final Optional<Dictionary> optDictionary = dictionaryLoader.load(event.dictionaries());
        if (optDictionary.isEmpty()) {
            presenter.presentSolverError("Dictionary not found");
            return;
        }

        final Optional<CrosswordSolver> optSolver = selectSolver(event);
        if (optSolver.isEmpty()) {
            presenter.presentSolverError("Solver not found");
            return;
        }

        final PuzzleDefinition puzzle = event.puzzle();
        final Dictionary dictionary = optDictionary.get();
        final ProgressListener progressListener = progressListenerFactory.from(event.progress());
        final CrosswordSolver solver = optSolver.get();

        runSolver(solver, puzzle, dictionary, progressListener);

    }

    /**
     * Runs the solver, handling potential {@link InterruptedException}.
     *
     * @param solver           the solver to run
     * @param puzzle           the puzzle to solve
     * @param dictionary       the dictionary to use
     * @param progressListener the progress listener
     */
    private void runSolver(final CrosswordSolver solver, final PuzzleDefinition puzzle,
                           final Dictionary dictionary, final ProgressListener progressListener) {
        try {
            final SolverResult result = solver.solve(puzzle, dictionary, progressListener);
            presenter.presentResult(result);
        } catch (final InterruptedException e) {
            presenter.presentSolverError(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Selects the solver to use given request parameters.
     *
     * @param request the solve request
     * @return the solver to use
     */
    private Optional<CrosswordSolver> selectSolver(final SolveRequest request) {
        final Optional<String> specifiedSolver = request.solver();
        if (specifiedSolver.isEmpty()) {
            // hasNext() unnecessary as constructor verifies that a list one solver exists
            final CrosswordSolver defaultSolver = solvers.values().iterator().next();
            return Optional.of(defaultSolver);
        }
        return Optional.ofNullable(solvers.get(specifiedSolver.get()));
    }

}