/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.solver;

import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import re.belv.croiseur.api.solver.SolveRequest;
import re.belv.croiseur.common.puzzle.ChangedPuzzle;
import re.belv.croiseur.common.puzzle.Puzzle;
import re.belv.croiseur.common.puzzle.PuzzleClues;
import re.belv.croiseur.common.puzzle.PuzzleDetails;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.common.puzzle.SavedPuzzle;
import re.belv.croiseur.impl.clue.shared.SafeClueProvider;
import re.belv.croiseur.impl.puzzle.persistence.shared.SafePuzzleRepository;
import re.belv.croiseur.impl.solver.postrun.SolverResultConverter;
import re.belv.croiseur.impl.solver.prerun.DictionaryLoader;
import re.belv.croiseur.impl.solver.prerun.ProgressListenerFactory;
import re.belv.croiseur.impl.solver.prerun.ShuffledSolverDictionary;
import re.belv.croiseur.spi.clue.ClueProvider;
import re.belv.croiseur.spi.dictionary.DictionaryProvider;
import re.belv.croiseur.spi.presenter.Presenter;
import re.belv.croiseur.spi.puzzle.repository.PuzzleRepository;
import re.belv.croiseur.spi.solver.CrosswordSolver;
import re.belv.croiseur.spi.solver.Dictionary;
import re.belv.croiseur.spi.solver.ProgressListener;
import re.belv.croiseur.spi.solver.SolverResult;

/** Solve usecase. */
final class SolveUsecase {

    /** The crossword solvers indexed by names. */
    private final Map<String, CrosswordSolver> solvers;

    /** The dictionary loader. */
    private final DictionaryLoader dictionaryLoader;

    /** The clue provider. */
    private final SafeClueProvider clueProvider;

    /** The puzzle repository. */
    private final SafePuzzleRepository puzzleRepository;

    /** The presenter. */
    private final Presenter presenter;

    /** A factory to create {@link ProgressListener}s from {@link SolveRequest}s. */
    private final ProgressListenerFactory progressListenerFactory;

    /**
     * Constructs an instance.
     *
     * @param solversArg the solvers
     * @param dictionaryProvidersArg the dictionary providers
     * @param presenterArg the presenter
     */
    SolveUsecase(
            final Collection<CrosswordSolver> solversArg,
            final Collection<DictionaryProvider> dictionaryProvidersArg,
            final Collection<ClueProvider> clueProvidersArg,
            final PuzzleRepository puzzleRepositoryArg,
            final Presenter presenterArg) {
        solvers = solversArg.stream().collect(toMap(CrosswordSolver::name, Function.identity()));
        clueProvider = new SafeClueProvider(clueProvidersArg, presenterArg);
        puzzleRepository = new SafePuzzleRepository(puzzleRepositoryArg, presenterArg);
        dictionaryLoader = new DictionaryLoader(dictionaryProvidersArg);
        progressListenerFactory = new ProgressListenerFactory(presenterArg);
        presenter = presenterArg;
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
        final Optional<SolverResult> optResult = runSolver(optSolver.get(), event.grid(), dictionary, progressListener);

        if (optResult.isPresent()) {
            final SolverResult solverResult = optResult.get();
            final re.belv.croiseur.spi.presenter.solver.SolverResult presentableResult =
                    SolverResultConverter.toPresentable(solverResult, event.grid());
            presenter.presentSolverResult(presentableResult);
            final Map<String, String> clues = optionallyGetClues(event, presentableResult);
            optionallyPresentClues(clues);
            optionallyUpdateSavedPuzzle(savedPuzzle, presentableResult, clues);
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
        final Puzzle puzzleToSave = new Puzzle(PuzzleDetails.emptyOfToday(), solveRequest.grid(), PuzzleClues.empty());
        return puzzleRepository.create(puzzleToSave);
    }

    /**
     * Returns a dictionary shuffled with the request's randomness source, if any, otherwise returns the given
     * dictionary as is.
     *
     * @param event the solve request
     * @param dictionary the non-shuffled dictionary
     * @return the dictionary shuffled with the request's randomness source, if any, otherwise returns the given
     *     dictionary as is.
     */
    private static Dictionary optionallyShuffledDictionary(final SolveRequest event, final Dictionary dictionary) {
        return event.dictionariesShuffle()
                .<Dictionary>map(random -> new ShuffledSolverDictionary(dictionary, random))
                .orElse(dictionary);
    }

    /**
     * Runs the solver, handling potential exceptions.
     *
     * @param solver the solver to run
     * @param puzzle the puzzle to solve
     * @param dictionary the dictionary to use
     * @param progressListener the progress listener
     */
    private Optional<SolverResult> runSolver(
            final CrosswordSolver solver,
            final PuzzleGrid puzzle,
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
     * Gets clues, if explicitly requested and if solver has found a solution.
     *
     * @param event the solve request
     * @param presentableResult the solver result
     * @return the clues, if any requested and found; otherwise, an empty map
     */
    private Map<String, String> optionallyGetClues(
            final SolveRequest event, final re.belv.croiseur.spi.presenter.solver.SolverResult presentableResult) {
        final Map<String, String> clues;
        if (event.withClues() && presentableResult.isSuccess()) {
            final Set<String> words = presentableResult.grid().slotContents();
            clues = clueProvider.getClues(words);
        } else {
            clues = Collections.emptyMap();
        }
        return clues;
    }

    /**
     * Present clues, if any.
     *
     * @param clues the clues to present
     */
    private void optionallyPresentClues(final Map<String, String> clues) {
        if (!clues.isEmpty()) {
            presenter.presentClues(clues);
        }
    }

    /**
     * Updates the previously saved puzzle, if any.
     *
     * @param savedPuzzleOpt the previously saved puzzle, if any
     * @param presentableResult the solver presentable result
     * @param clues the clues
     */
    private void optionallyUpdateSavedPuzzle(
            final Optional<SavedPuzzle> savedPuzzleOpt,
            final re.belv.croiseur.spi.presenter.solver.SolverResult presentableResult,
            final Map<String, String> clues) {
        if (savedPuzzleOpt.isPresent() && presentableResult.isSuccess()) {
            final SavedPuzzle savedPuzzle = savedPuzzleOpt.get();
            final PuzzleGrid newGrid = presentableResult.grid();
            final PuzzleClues newClues = PuzzleClues.from(clues, newGrid);
            final ChangedPuzzle changedPuzzle = savedPuzzle.modifiedWith(newGrid, newClues);
            puzzleRepository.update(changedPuzzle);
        } // else no previously saved puzzle, do nothing
    }
}
