/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.presenter;

import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryProviderDetails;
import com.gitlab.super7ramp.croiseur.common.dictionary.ProvidedDictionaryDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryContent;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionarySearchResult;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverDescription;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverInitialisationState;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverProgress;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverResult;

import java.util.Collection;
import java.util.List;

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

    /**
     * Constructs an instance.
     */
    public CliPresenter() {
        cliSolverPresenter = new CliSolverPresenter();
        cliDictionaryPresenter = new CliDictionaryPresenter();
        cliPuzzlePresenter = new CliPuzzlePresenter();
    }

    @Override
    public void presentAvailableSolvers(final List<SolverDescription> solverDescriptions) {
        cliSolverPresenter.presentAvailableSolvers(solverDescriptions);
    }

    @Override
    public void presentSolverInitialisationState(
            final SolverInitialisationState solverInitialisationState) {
        cliSolverPresenter.presentSolverInitialisationState(solverInitialisationState);
    }

    @Override
    public void presentSolverProgress(final SolverProgress progress) {
        cliSolverPresenter.presentSolverProgress(progress);
    }

    @Override
    public void presentSolverResult(final SolverResult result) {
        cliSolverPresenter.presentSolverResult(result);
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
}
