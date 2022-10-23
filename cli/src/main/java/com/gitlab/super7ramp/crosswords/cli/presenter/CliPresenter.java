package com.gitlab.super7ramp.crosswords.cli.presenter;

import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.presenter.Presenter;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

import java.util.Collection;

/**
 * CLI implementation of {@link Presenter}.
 */
public final class CliPresenter implements Presenter {

    /** The {@link CliSolverPresenter}. */
    private final CliSolverPresenter cliSolverPresenter;

    /** The {@link CliDictionaryPresenter}. */
    private final CliDictionaryPresenter cliDictionaryPresenter;

    /**
     * Constructor.
     */
    public CliPresenter() {
        cliSolverPresenter = new CliSolverPresenter();
        cliDictionaryPresenter = new CliDictionaryPresenter();
    }

    @Override
    public void publishSolverInitialisationState(final SolverInitialisationState solverInitialisationState) {
        cliSolverPresenter.publishSolverInitialisationState(solverInitialisationState);
    }

    @Override
    public void publishProgress(final short completionPercentage) {
        cliSolverPresenter.publishProgress(completionPercentage);
    }

    @Override
    public void publishResult(final SolverResult result) {
        cliSolverPresenter.publishResult(result);
    }

    @Override
    public void publishError(final String error) {
        cliSolverPresenter.publishError(error);
    }

    @Override
    public void publishDictionaryProviders(final Collection<DictionaryProvider> providers) {
        cliDictionaryPresenter.publishDictionaryProviders(providers);
    }

    @Override
    public void publishDictionaries(final Collection<DictionaryProvider> filteredDictionaryProviders) {
        cliDictionaryPresenter.publishDictionaries(filteredDictionaryProviders);
    }

    @Override
    public void publishDictionaryEntries(final Dictionary dictionary) {
        cliDictionaryPresenter.publishDictionaryEntries(dictionary);
    }
}
