package com.gitlab.super7ramp.crosswords.gui.presenter;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryDescription;
import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.crosswords.gui.view.model.CrosswordSolverViewModel;
import com.gitlab.super7ramp.crosswords.spi.presenter.Presenter;
import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverInitialisationState;
import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverProgress;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * GUI implementation of {@link Presenter}.
 */
public final class GuiPresenter implements Presenter {

    /** Dictionary presenter. */
    private final GuiDictionaryPresenter dictionaryPresenter;

    /** Crossword presenter. */
    private final GuiSolverPresenter crosswordPresenter;

    /**
     * Constructs an instance.
     *
     * @param crosswordSolverViewModel the view model
     */
    public GuiPresenter(final CrosswordSolverViewModel crosswordSolverViewModel) {
        dictionaryPresenter =
                new GuiDictionaryPresenter(crosswordSolverViewModel.dictionaryViewModel());
        crosswordPresenter =
                new GuiSolverPresenter(crosswordSolverViewModel.crosswordGridViewModel());
    }

    @Override
    public void presentSolverInitialisationState(final SolverInitialisationState solverInitialisationState) {
        crosswordPresenter.presentSolverInitialisationState(solverInitialisationState);
    }

    @Override
    public void presentProgress(final SolverProgress solverProgress) {
        crosswordPresenter.presentProgress(solverProgress);
    }

    @Override
    public void presentResult(final SolverResult result) {
        crosswordPresenter.presentResult(result);
    }

    @Override
    public void presentError(final String error) {
        crosswordPresenter.presentError(error);
    }

    @Override
    public void presentDictionaryProviders(final Collection<DictionaryProviderDescription> providers) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentDictionaries(final Map<DictionaryProviderDescription, Collection<?
            extends DictionaryDescription>> dictionaries) {
        dictionaryPresenter.presentDictionaries(dictionaries);
    }

    @Override
    public void presentDictionaryEntries(final List<String> entries) {
        dictionaryPresenter.presentDictionaryEntries(entries);
    }

    @Override
    public void presentPreferredDictionary(final DictionaryDescription preferredDictionary) {
        dictionaryPresenter.presentPreferredDictionary(preferredDictionary);
    }
}
