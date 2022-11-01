package com.gitlab.super7ramp.crosswords.gui.presenter;

import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordSolverViewModel;
import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryDescription;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.crosswords.spi.presenter.Presenter;
import com.gitlab.super7ramp.crosswords.spi.presenter.SolverInitialisationState;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

import java.util.Collection;
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
    public void presentProgress(final short completionPercentage) {
        crosswordPresenter.presentProgress(completionPercentage);
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
    public void presentDictionaryEntries(final Dictionary dictionary) {
        dictionaryPresenter.presentDictionaryEntries(dictionary);
    }

    @Override
    public void presentPreferredDictionary(final DictionaryDescription preferredDictionary) {
        dictionaryPresenter.presentPreferredDictionary(preferredDictionary);
    }
}
