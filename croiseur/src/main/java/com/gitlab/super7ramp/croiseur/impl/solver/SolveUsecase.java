/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.solver;

import com.gitlab.super7ramp.croiseur.api.solver.SolveRequest;
import com.gitlab.super7ramp.croiseur.common.PuzzleDefinition;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;
import com.gitlab.super7ramp.croiseur.spi.solver.Dictionary;
import com.gitlab.super7ramp.croiseur.spi.solver.ProgressListener;
import com.gitlab.super7ramp.croiseur.spi.solver.SolverResult;

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
        solvers = solversArg.stream().collect(toMap(CrosswordSolver::name, Function.identity()));
        presenter = presenterArg;
        dictionaryLoader = new DictionaryLoader(dictionaryProvidersArg);
        progressListenerFactory = new ProgressListenerFactory(presenter);
    }

    /**
     * Processes the given {@link SolveRequest}.
     *
     * @param event the solve request to process
     */
    void process(final SolveRequest event) {

        final Optional<CrosswordSolver> optSolver = selectSolver(event);
        if (optSolver.isEmpty()) {
            presenter.presentSolverError("Solver not found");
            return;
        }

        final Optional<Dictionary> optDictionary = dictionaryLoader.load(event.dictionaries());
        if (optDictionary.isEmpty()) {
            presenter.presentSolverError("Dictionary not found");
            return;
        }

        final CrosswordSolver solver = optSolver.get();
        final PuzzleDefinition puzzle = event.puzzle();
        final Dictionary dictionary = optionallyShuffledDictionary(event, optDictionary.get());
        final ProgressListener progressListener = progressListenerFactory.from(event.progress());

        runSolver(solver, puzzle, dictionary, progressListener);

    }

    /**
     * Returns an optionally shuffled dictionary, if requested.
     *
     * @param event      the solve request
     * @param dictionary the non-shuffled dictionary
     * @return the dictionary shuffled, if requested
     */
    private static Dictionary optionallyShuffledDictionary(final SolveRequest event,
                                                           final Dictionary dictionary) {
        return event.dictionariesShuffle()
                    .<Dictionary>map(random -> new ShuffledSolverDictionary(dictionary, random))
                    .orElse(dictionary);
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
        return request.solver().map(solvers::get).or(solvers.values().stream()::findFirst);
    }

}
