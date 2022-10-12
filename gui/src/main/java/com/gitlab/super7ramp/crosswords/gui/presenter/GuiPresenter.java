package com.gitlab.super7ramp.crosswords.gui.presenter;

import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordViewModel;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.DictionaryViewModel;
import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.presenter.Presenter;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

import java.util.Collection;

/**
 * Crosswords GUI Presenter.
 */
public final class GuiPresenter implements Presenter {

    private static final DictionaryPresenter dictionaryPresenter = new DictionaryPresenter();

    private static final CrosswordPresenter crosswordPresenter = new CrosswordPresenter();

    public GuiPresenter() {
        // nothing to do
    }

    public static void inject(CrosswordViewModel crosswordViewModel,
                              DictionaryViewModel dictionaryViewModelArg) {
        crosswordPresenter.inject(crosswordViewModel);
        dictionaryPresenter.inject(dictionaryViewModelArg);
    }

    @Override
    public void publishResult(SolverResult result) {
        crosswordPresenter.presentSolverResult(result);
    }

    @Override
    public void publishError(String error) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void publishDictionaryProviders(Collection<DictionaryProvider> providers) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void publishDictionaries(Collection<DictionaryProvider> filteredDictionaryProviders) {
        dictionaryPresenter.presentDictionaries(filteredDictionaryProviders);
    }

    @Override
    public void publishDictionaryEntries(Dictionary dictionary) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
