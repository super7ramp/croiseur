package com.gitlab.super7ramp.crosswords.cli.presenter;

import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryDescription;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.crosswords.spi.presenter.Presenter;
import com.gitlab.super7ramp.crosswords.spi.presenter.SolverInitialisationState;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

import java.util.Collection;
import java.util.Map;

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
    public void presentSolverInitialisationState(final SolverInitialisationState solverInitialisationState) {
        cliSolverPresenter.presentSolverInitialisationState(solverInitialisationState);
    }

    @Override
    public void presentProgress(final short completionPercentage) {
        cliSolverPresenter.presentProgress(completionPercentage);
    }

    @Override
    public void presentResult(final SolverResult result) {
        cliSolverPresenter.presentResult(result);
    }

    @Override
    public void presentError(final String error) {
        cliSolverPresenter.presentError(error);
    }

    @Override
    public void presentDictionaryProviders(final Collection<DictionaryProviderDescription> providers) {
        cliDictionaryPresenter.presentDictionaryProviders(providers);
    }

    @Override
    public void presentDictionaries(final Map<DictionaryProviderDescription, Collection<?
            extends DictionaryDescription>> dictionariesPerProviders) {
        cliDictionaryPresenter.presentDictionaries(dictionariesPerProviders);
    }

    @Override
    public void presentDictionaryEntries(final Dictionary dictionary) {
        cliDictionaryPresenter.presentDictionaryEntries(dictionary);
    }

    @Override
    public void presentPreferredDictionary(final DictionaryDescription preferredDictionary) {
        cliDictionaryPresenter.presentPreferredDictionary(preferredDictionary);
    }
}
