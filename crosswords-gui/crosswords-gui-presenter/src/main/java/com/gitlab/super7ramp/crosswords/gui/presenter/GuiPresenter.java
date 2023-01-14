/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.gui.presenter;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.crosswords.common.dictionary.ProvidedDictionaryDescription;
import com.gitlab.super7ramp.crosswords.gui.view.model.CrosswordSolverViewModel;
import com.gitlab.super7ramp.crosswords.spi.presenter.Presenter;
import com.gitlab.super7ramp.crosswords.spi.presenter.dictionary.DictionaryContent;
import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverDescription;
import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverInitialisationState;
import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverProgress;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

import java.util.Collection;
import java.util.List;

/**
 * GUI implementation of {@link Presenter}.
 */
public final class GuiPresenter implements Presenter {

    /** Dictionary presenter. */
    private final GuiDictionaryPresenter dictionaryPresenter;

    /** Solver presenter. */
    private final GuiSolverPresenter solverPresenter;

    /**
     * Constructs an instance.
     *
     * @param crosswordSolverViewModel the view model
     */
    public GuiPresenter(final CrosswordSolverViewModel crosswordSolverViewModel) {
        dictionaryPresenter =
                new GuiDictionaryPresenter(crosswordSolverViewModel.dictionaryViewModel());
        solverPresenter =
                new GuiSolverPresenter(crosswordSolverViewModel.crosswordGridViewModel());
    }

    @Override
    public void presentAvailableSolvers(final List<SolverDescription> solverDescriptions) {
        solverPresenter.presentAvailableSolvers(solverDescriptions);
    }

    @Override
    public void presentSolverInitialisationState(final SolverInitialisationState solverInitialisationState) {
        solverPresenter.presentSolverInitialisationState(solverInitialisationState);
    }

    @Override
    public void presentProgress(final SolverProgress solverProgress) {
        solverPresenter.presentProgress(solverProgress);
    }

    @Override
    public void presentResult(final SolverResult result) {
        solverPresenter.presentResult(result);
    }

    @Override
    public void presentSolverError(final String error) {
        solverPresenter.presentSolverError(error);
    }

    @Override
    public void presentDictionaryProviders(final Collection<DictionaryProviderDescription> providers) {
        dictionaryPresenter.presentDictionaryProviders(providers);
    }

    @Override
    public void presentDictionaries(final List<ProvidedDictionaryDescription> dictionaries) {
        dictionaryPresenter.presentDictionaries(dictionaries);
    }

    @Override
    public void presentDictionaryEntries(final DictionaryContent content) {
        dictionaryPresenter.presentDictionaryEntries(content);
    }

    @Override
    public void presentPreferredDictionary(final ProvidedDictionaryDescription preferredDictionary) {
        dictionaryPresenter.presentPreferredDictionary(preferredDictionary);
    }

    @Override
    public void presentDictionaryError(final String error) {
        dictionaryPresenter.presentDictionaryError(error);
    }
}
