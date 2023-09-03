/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli.presenter;

import re.belv.croiseur.common.dictionary.DictionaryProviderDetails;
import re.belv.croiseur.common.dictionary.ProvidedDictionaryDetails;
import re.belv.croiseur.common.puzzle.PuzzleCodecDetails;
import re.belv.croiseur.common.puzzle.SavedPuzzle;
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
 * CLI implementation of {@link Presenter}.
 */
public final class CliPresenter implements Presenter {

    /** The {@link CliSolverPresenter}. */
    private final CliSolverPresenter cliSolverPresenter;

    /** The {@link CliDictionaryPresenter}. */
    private final CliDictionaryPresenter cliDictionaryPresenter;

    /** The {@link CliPuzzlePresenter}. */
    private final CliPuzzlePresenter cliPuzzlePresenter;

    /** The {@link CliCluePresenter}. */
    private final CliCluePresenter cliCluePresenter;

    /**
     * Constructs an instance.
     */
    public CliPresenter() {
        cliSolverPresenter = new CliSolverPresenter();
        cliDictionaryPresenter = new CliDictionaryPresenter();
        cliPuzzlePresenter = new CliPuzzlePresenter();
        cliCluePresenter = new CliCluePresenter();
    }

    @Override
    public void presentAvailableSolvers(final List<SolverDescription> solverDescriptions) {
        cliSolverPresenter.presentAvailableSolvers(solverDescriptions);
    }

    @Override
    public void presentSolverInitialisationState(final String solverRun,
                                                 final SolverInitialisationState solverInitialisationState) {
        cliSolverPresenter.presentSolverInitialisationState(solverRun, solverInitialisationState);
    }

    @Override
    public void presentSolverProgress(final String solverRun, final SolverProgress progress) {
        cliSolverPresenter.presentSolverProgress(solverRun, progress);
    }

    @Override
    public void presentSolverResult(final String solverRun, final SolverResult result) {
        cliSolverPresenter.presentSolverResult(solverRun, result);
    }

    @Override
    public void presentSolverError(final String solverRun, final String error) {
        cliSolverPresenter.presentSolverError(solverRun, error);
    }

    @Override
    public void presentSolverError(final String error) {
        cliSolverPresenter.presentSolverError(error);
    }

    @Override
    public void presentDictionaryProviders(final Collection<DictionaryProviderDetails> providers) {
        cliDictionaryPresenter.presentDictionaryProviders(providers);
    }

    @Override
    public void presentDictionaries(final List<ProvidedDictionaryDetails> dictionaries) {
        cliDictionaryPresenter.presentDictionaries(dictionaries);
    }

    @Override
    public void presentDictionaryEntries(final DictionaryContent content) {
        cliDictionaryPresenter.presentDictionaryEntries(content);
    }

    @Override
    public void presentDictionarySearchResult(final DictionarySearchResult searchResult) {
        cliDictionaryPresenter.presentDictionarySearchResult(searchResult);
    }

    @Override
    public void presentDefaultDictionary(final ProvidedDictionaryDetails defaultDictionary) {
        cliDictionaryPresenter.presentDefaultDictionary(defaultDictionary);
    }

    @Override
    public void presentDictionaryError(final String error) {
        cliDictionaryPresenter.presentDictionaryError(error);
    }

    @Override
    public void presentAvailablePuzzles(final List<SavedPuzzle> puzzles) {
        cliPuzzlePresenter.presentAvailablePuzzles(puzzles);
    }

    @Override
    public void presentLoadedPuzzle(final SavedPuzzle puzzle) {
        cliPuzzlePresenter.presentLoadedPuzzle(puzzle);
    }

    @Override
    public void presentPuzzleRepositoryError(final String error) {
        cliPuzzlePresenter.presentPuzzleRepositoryError(error);
    }

    @Override
    public void presentSavedPuzzle(final SavedPuzzle puzzle) {
        cliPuzzlePresenter.presentSavedPuzzle(puzzle);
    }

    @Override
    public void presentDeletedAllPuzzles() {
        cliPuzzlePresenter.presentDeletedAllPuzzles();
    }

    @Override
    public void presentDeletedPuzzle(final long id) {
        cliPuzzlePresenter.presentDeletedPuzzle(id);
    }

    @Override
    public void presentPuzzleDecoders(final List<PuzzleCodecDetails> decoders) {
        cliPuzzlePresenter.presentPuzzleDecoders(decoders);
    }

    @Override
    public void presentPuzzleImportError(final String error) {
        cliPuzzlePresenter.presentPuzzleImportError(error);
    }

    @Override
    public void presentPuzzleEncoders(final List<PuzzleCodecDetails> encoders) {
        cliPuzzlePresenter.presentPuzzleEncoders(encoders);
    }

    @Override
    public void presentPuzzleExportError(final String error) {
        cliPuzzlePresenter.presentPuzzleExportError(error);
    }

    @Override
    public void presentClueError(final String error) {
        cliCluePresenter.presentClueError(error);
    }

    @Override
    public void presentClueProviders(final List<ClueProviderDescription> clueProviderDescriptions) {
        cliCluePresenter.presentClueProviders(clueProviderDescriptions);
    }

    @Override
    public void presentClues(final Map<String, String> clues) {
        cliCluePresenter.presentClues(clues);
    }
}
