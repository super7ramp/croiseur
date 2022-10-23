package com.gitlab.super7ramp.crosswords.gui.presenter;

import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordSolverViewModel;
import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.presenter.Presenter;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

import java.util.Collection;
import java.util.logging.Logger;

/**
 * Crosswords GUI Presenter.
 */
public final class GuiPresenter implements Presenter {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(GuiPresenter.class.getName());

    /** Dictionary presenter. */
    private final DictionaryPresenter dictionaryPresenter;

    /** Crossword presenter. */
    private final CrosswordPresenter crosswordPresenter;

    /**
     * Constructs an instance.
     *
     * @param crosswordSolverViewModel the view model
     */
    public GuiPresenter(final CrosswordSolverViewModel crosswordSolverViewModel) {
        dictionaryPresenter =
                new DictionaryPresenter(crosswordSolverViewModel.dictionaryViewModel());
        crosswordPresenter = new CrosswordPresenter(crosswordSolverViewModel.crosswordViewModel());
    }

    @Override
    public void publishSolverInitialisationState(final SolverInitialisationState solverInitialisationState) {
        // TODO really implement
        LOGGER.info(() -> "Solver initialisation: " + solverInitialisationState);
    }

    @Override
    public void publishProgress(final short completionPercentage) {
        // TODO really implement
        LOGGER.info(() -> "Completion: " + completionPercentage + " %");
    }

    @Override
    public void publishResult(final SolverResult result) {
        crosswordPresenter.presentSolverResult(result);
    }

    @Override
    public void publishError(final String error) {
        // TODO really implement
        LOGGER.warning(error);
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
