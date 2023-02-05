/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.presenter;

import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.croiseur.common.dictionary.ProvidedDictionaryDescription;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.clue.ClueProviderDescription;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryContent;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverDescription;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverInitialisationState;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverProgress;
import com.gitlab.super7ramp.croiseur.spi.solver.SolverResult;

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

    /** The {@link CliCluePresenter}. */
    private final CliCluePresenter cliCluePresenter;

    /**
     * Constructor.
     */
    public CliPresenter() {
        cliSolverPresenter = new CliSolverPresenter();
        cliDictionaryPresenter = new CliDictionaryPresenter();
        cliCluePresenter = new CliCluePresenter();
    }

    @Override
    public void presentAvailableSolvers(final List<SolverDescription> solverDescriptions) {
        cliSolverPresenter.presentAvailableSolvers(solverDescriptions);
    }

    @Override
    public void presentSolverInitialisationState(final SolverInitialisationState solverInitialisationState) {
        cliSolverPresenter.presentSolverInitialisationState(solverInitialisationState);
    }

    @Override
    public void presentProgress(final SolverProgress progress) {
        cliSolverPresenter.presentProgress(progress);
    }

    @Override
    public void presentResult(final SolverResult result) {
        cliSolverPresenter.presentResult(result);
    }

    @Override
    public void presentSolverError(final String error) {
        cliSolverPresenter.presentSolverError(error);
    }

    @Override
    public void presentDictionaryProviders(final Collection<DictionaryProviderDescription> providers) {
        cliDictionaryPresenter.presentDictionaryProviders(providers);
    }

    @Override
    public void presentDictionaries(final List<ProvidedDictionaryDescription> dictionaries) {
        cliDictionaryPresenter.presentDictionaries(dictionaries);
    }

    @Override
    public void presentDictionaryEntries(final DictionaryContent content) {
        cliDictionaryPresenter.presentDictionaryEntries(content);
    }

    @Override
    public void presentPreferredDictionary(final ProvidedDictionaryDescription preferredDictionary) {
        cliDictionaryPresenter.presentPreferredDictionary(preferredDictionary);
    }

    @Override
    public void presentDictionaryError(final String error) {
        cliDictionaryPresenter.presentDictionaryError(error);
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
