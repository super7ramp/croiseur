/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.solver;

import com.gitlab.super7ramp.croiseur.api.solver.SolveRequest;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.impl.puzzle.repository.SafePuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.PuzzleRepository;
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

    /** The puzzle repository. */
    private final SafePuzzleRepository puzzleRepository;

    /** The presenter. */
    private final Presenter presenter;

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
                 final PuzzleRepository puzzleRepositoryArg,
                 final Presenter presenterArg) {
        solvers = solversArg.stream().collect(toMap(CrosswordSolver::name, Function.identity()));
        presenter = presenterArg;
        puzzleRepository = new SafePuzzleRepository(puzzleRepositoryArg, presenterArg);
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

        final Optional<SavedPuzzle> savedPuzzle = optionallySavePuzzle(event);

        final Dictionary dictionary = optionallyShuffledDictionary(event, optDictionary.get());
        final ProgressListener progressListener = progressListenerFactory.from(event.progress());
        final Optional<SolverResult> result =
                runSolver(optSolver.get(), event.grid(), dictionary, progressListener);
        result.ifPresent(presenter::presentSolverResult);

        optionallyUpdateSavedPuzzle(savedPuzzle, result);
    }

    /**
     * Updates the previously saved puzzle, if result is present and successful.
     *
     * @param savedPuzzleOpt  the previously saved puzzle, if any
     * @param solverResultOpt the solver run result, if any
     */
    private void optionallyUpdateSavedPuzzle(final Optional<SavedPuzzle> savedPuzzleOpt,
                                             final Optional<SolverResult> solverResultOpt) {
        if (savedPuzzleOpt.isPresent() &&
            solverResultOpt.filter(result -> result.kind().isSuccess()).isPresent()) {

            final SavedPuzzle savedPuzzle = savedPuzzleOpt.get();
            final PuzzleGrid savedPuzzleGrid = savedPuzzle.grid();
            final SolverResult result = solverResultOpt.get();
            final PuzzleGrid updatedGrid =
                    new PuzzleGrid(savedPuzzleGrid.width(), savedPuzzleGrid.height(),
                                   savedPuzzleGrid.shaded(), result.filledBoxes());
            final Puzzle updatedPuzzle = new Puzzle(savedPuzzle.details(), updatedGrid);
            puzzleRepository.update(savedPuzzle.modifiedWith(updatedPuzzle));

        } // else no previously saved puzzle, do nothing

    }

    /**
     * Adds the puzzle from solve request to puzzle repository, if applicable.
     *
     * @param solveRequest the solve request to process
     * @return the puzzle saved to repository, if applicable
     */
    private Optional<SavedPuzzle> optionallySavePuzzle(final SolveRequest solveRequest) {
        if (!solveRequest.savePuzzle()) {
            return Optional.empty();
        }
        final Puzzle puzzleToSave = new Puzzle(PuzzleDetails.emptyOfToday(), solveRequest.grid());
        return puzzleRepository.create(puzzleToSave);
    }

    /**
     * Returns a dictionary shuffled with the request's randomness source, if any, otherwise returns
     * the given dictionary as is.
     *
     * @param event      the solve request
     * @param dictionary the non-shuffled dictionary
     * @return the dictionary shuffled with the request's randomness source, if any, otherwise
     * returns the given dictionary as is.
     */
    private static Dictionary optionallyShuffledDictionary(final SolveRequest event,
                                                           final Dictionary dictionary) {
        return event.dictionariesShuffle()
                    .<Dictionary>map(random -> new ShuffledSolverDictionary(dictionary, random))
                    .orElse(dictionary);
    }

    /**
     * Runs the solver, handling potential exceptions.
     *
     * @param solver           the solver to run
     * @param puzzle           the puzzle to solve
     * @param dictionary       the dictionary to use
     * @param progressListener the progress listener
     */
    private Optional<SolverResult> runSolver(final CrosswordSolver solver, final PuzzleGrid puzzle,
                                             final Dictionary dictionary,
                                             final ProgressListener progressListener) {
        try {
            final SolverResult result = solver.solve(puzzle, dictionary, progressListener);
            return Optional.of(result);
        } catch (final InterruptedException e) {
            // Do not present an error as interruption is likely to have been triggered by user
            Thread.currentThread().interrupt();
            return Optional.empty();
        } catch (final Exception e) {
            /*
             * Present exception message, even for runtime exceptions: Exception comes from only one
             * solver plugin, it should not stop the whole application.
             */
            presenter.presentSolverError(e.getMessage());
            return Optional.empty();
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
