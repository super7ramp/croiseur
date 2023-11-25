/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.web.presenter;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import re.belv.croiseur.common.dictionary.DictionaryProviderDetails;
import re.belv.croiseur.common.dictionary.ProvidedDictionaryDetails;
import re.belv.croiseur.common.puzzle.PuzzleCodecDetails;
import re.belv.croiseur.common.puzzle.SavedPuzzle;
import re.belv.croiseur.spi.presenter.Presenter;
import re.belv.croiseur.spi.presenter.clue.ClueProviderDescription;
import re.belv.croiseur.spi.presenter.dictionary.DictionaryContent;
import re.belv.croiseur.spi.presenter.dictionary.DictionarySearchResult;
import re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import re.belv.croiseur.spi.presenter.solver.SolverDescription;
import re.belv.croiseur.spi.presenter.solver.SolverInitialisationState;
import re.belv.croiseur.spi.presenter.solver.SolverPresenter;
import re.belv.croiseur.spi.presenter.solver.SolverProgress;
import re.belv.croiseur.spi.presenter.solver.SolverResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link Presenter} for Croiseur Web.
 * <p>
 * This class is instantiated by {@link java.util.ServiceLoader ServiceLoader} by core service so
 * dependencies cannot be injected directly: It uses the Spring {@link ApplicationContext}, which is
 * expected to be statically set before instantiation, to retrieve the Spring beans.
 */
@Component
public class WebPresenter implements Presenter {

    /** The solver presenter. */
    private final SolverPresenter solverPresenter;

    /** The puzzle presenter. */
    private final PuzzlePresenter puzzlePresenter;

    /**
     * Constructs an instance.
     *
     * @param solverPresenterArg the solver presenter
     * @param puzzlePresenterArg the puzzle presenter
     */
    public WebPresenter(final SolverPresenter solverPresenterArg,
                        final PuzzlePresenter puzzlePresenterArg) {
        solverPresenter = solverPresenterArg;
        puzzlePresenter = puzzlePresenterArg;
    }

    @Override
    public void presentClueError(final String error) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentClueProviders(final List<ClueProviderDescription> clueProviderDescriptions) {
        throw new UnsupportedOperationException("Not implemented yet");

    }

    @Override
    public void presentClues(final Map<String, String> clues) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentDictionaryProviders(final Collection<DictionaryProviderDetails> providers) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentDictionaries(final List<ProvidedDictionaryDetails> dictionaries) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentDictionaryEntries(final DictionaryContent content) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentDictionarySearchResult(final DictionarySearchResult searchResult) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentDefaultDictionary(final ProvidedDictionaryDetails defaultDictionary) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentDictionaryError(final String error) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentAvailablePuzzles(final List<SavedPuzzle> puzzles) {
        puzzlePresenter.presentAvailablePuzzles(puzzles);
    }

    @Override
    public void presentLoadedPuzzle(final SavedPuzzle puzzle) {
        puzzlePresenter.presentLoadedPuzzle(puzzle);
    }

    @Override
    public void presentPuzzleRepositoryError(final String error) {
        puzzlePresenter.presentPuzzleRepositoryError(error);
    }

    @Override
    public void presentSavedPuzzle(final SavedPuzzle puzzle) {
        puzzlePresenter.presentSavedPuzzle(puzzle);
    }

    @Override
    public void presentDeletedAllPuzzles() {
        puzzlePresenter.presentDeletedAllPuzzles();
    }

    @Override
    public void presentDeletedPuzzle(final long id) {
        puzzlePresenter.presentDeletedPuzzle(id);
    }

    @Override
    public void presentPuzzleDecoders(final List<PuzzleCodecDetails> decoders) {
        puzzlePresenter.presentPuzzleDecoders(decoders);
    }

    @Override
    public void presentPuzzleImportError(final String error) {
        puzzlePresenter.presentPuzzleImportError(error);
    }

    @Override
    public void presentPuzzleEncoders(final List<PuzzleCodecDetails> encoders) {
        puzzlePresenter.presentPuzzleEncoders(encoders);
    }

    @Override
    public void presentPuzzleExportError(final String error) {
        puzzlePresenter.presentPuzzleExportError(error);
    }

    @Override
    public void presentAvailableSolvers(final List<SolverDescription> solverDescriptions) {
        solverPresenter.presentAvailableSolvers(solverDescriptions);
    }

    @Override
    public void presentSolverInitialisationState(final String solverRun,
                                                 final SolverInitialisationState solverInitialisationState) {
        solverPresenter.presentSolverInitialisationState(solverRun, solverInitialisationState);
    }

    @Override
    public void presentSolverProgress(final String solverRun, final SolverProgress progress) {
        solverPresenter.presentSolverProgress(solverRun, progress);
    }

    @Override
    public void presentSolverResult(final String solverRun, final SolverResult result) {
        solverPresenter.presentSolverResult(solverRun, result);
    }

    @Override
    public void presentSolverError(final String solverRun, final String error) {
        solverPresenter.presentSolverError(solverRun, error);
    }

    @Override
    public void presentSolverError(final String error) {
        solverPresenter.presentSolverError(error);
    }

}
