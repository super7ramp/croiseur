/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.presenter;

import re.belv.croiseur.common.dictionary.DictionaryProviderDetails;
import re.belv.croiseur.common.dictionary.ProvidedDictionaryDetails;
import re.belv.croiseur.common.puzzle.PuzzleCodecDetails;
import re.belv.croiseur.common.puzzle.SavedPuzzle;
import re.belv.croiseur.gui.view.model.ApplicationViewModel;
import re.belv.croiseur.spi.presenter.Presenter;
import re.belv.croiseur.spi.presenter.clue.ClueProviderDescription;
import re.belv.croiseur.spi.presenter.dictionary.DictionaryContent;
import re.belv.croiseur.spi.presenter.dictionary.DictionarySearchResult;
import re.belv.croiseur.spi.presenter.solver.SolverDescription;
import re.belv.croiseur.spi.presenter.solver.SolverInitialisationState;
import re.belv.croiseur.spi.presenter.solver.SolverProgress;
import re.belv.croiseur.spi.presenter.solver.SolverResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * GUI implementation of {@link Presenter}.
 */
public final class GuiPresenter implements Presenter {

    /** Dictionary presenter. */
    private final GuiDictionaryPresenter dictionaryPresenter;

    /** Solver presenter. */
    private final GuiSolverPresenter solverPresenter;

    /** Clue presenter. */
    private final GuiCluePresenter cluePresenter;

    /** Puzzle presenter. */
    private final GuiPuzzlePresenter puzzlePresenter;

    /**
     * Constructs an instance.
     *
     * @param applicationViewModel the view model
     */
    public GuiPresenter(final ApplicationViewModel applicationViewModel) {
        dictionaryPresenter =
                new GuiDictionaryPresenter(applicationViewModel.dictionaryViewModel(),
                                           applicationViewModel.errorsViewModel());
        solverPresenter =
                new GuiSolverPresenter(applicationViewModel.crosswordGridViewModel(),
                                       applicationViewModel.solverConfigurationViewModel(),
                                       applicationViewModel.solverProgressViewModel(),
                                       applicationViewModel.errorsViewModel());
        cluePresenter = new GuiCluePresenter(applicationViewModel.cluesViewModel(),
                                             applicationViewModel.crosswordGridViewModel(),
                                             applicationViewModel.errorsViewModel());
        puzzlePresenter = new GuiPuzzlePresenter(applicationViewModel.puzzleSelectionViewModel(),
                                                 applicationViewModel.puzzleEditionViewModel(),
                                                 applicationViewModel.puzzleCodecsViewModel(),
                                                 applicationViewModel.errorsViewModel());
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
    public void presentSolverProgress(final String solverRun, final SolverProgress solverProgress) {
        solverPresenter.presentSolverProgress(solverRun, solverProgress);
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

    @Override
    public void presentDictionaryProviders(final Collection<DictionaryProviderDetails> providers) {
        dictionaryPresenter.presentDictionaryProviders(providers);
    }

    @Override
    public void presentDictionaries(final List<ProvidedDictionaryDetails> dictionaries) {
        dictionaryPresenter.presentDictionaries(dictionaries);
    }

    @Override
    public void presentDictionaryEntries(final DictionaryContent content) {
        dictionaryPresenter.presentDictionaryEntries(content);
    }

    @Override
    public void presentDictionarySearchResult(final DictionarySearchResult searchResult) {
        dictionaryPresenter.presentDictionarySearchResult(searchResult);
    }

    @Override
    public void presentDefaultDictionary(final ProvidedDictionaryDetails defaultDictionary) {
        dictionaryPresenter.presentDefaultDictionary(defaultDictionary);
    }

    @Override
    public void presentDictionaryError(final String error) {
        dictionaryPresenter.presentDictionaryError(error);
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
    public void presentClueError(final String error) {
        cluePresenter.presentClueError(error);
    }

    @Override
    public void presentClueProviders(final List<ClueProviderDescription> clueProviderDescriptions) {
        cluePresenter.presentClueProviders(clueProviderDescriptions);
    }

    @Override
    public void presentClues(final Map<String, String> clues) {
        cluePresenter.presentClues(clues);
    }
}
