/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.dictionary;

import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.croiseur.api.dictionary.ListDictionariesRequest;
import com.gitlab.super7ramp.croiseur.api.dictionary.ListDictionaryEntriesRequest;
import com.gitlab.super7ramp.croiseur.api.dictionary.SearchDictionaryEntriesRequest;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;

import java.util.Collection;

/**
 * Implementation of {@link DictionaryService}.
 */
public final class DictionaryServiceImpl implements DictionaryService {

    /** The 'list dictionary providers' usecase. */
    private final ListDictionaryProvidersUsecase listDictionaryProvidersUsecase;

    /** The 'list dictionaries' usecase. */
    private final ListDictionariesUsecase listDictionariesUsecase;

    /** The 'list dictionary entries' usecase. */
    private final ListDictionaryEntriesUsecase listDictionaryEntries;

    /** The 'search dictionary entries' usecase. */
    private final SearchDictionaryEntriesUsecase searchDictionaryEntries;

    /** The 'show preferred dictionary' usecase. */
    private final ShowPreferredDictionaryUsecase showPreferredDictionaryUsecase;

    /**
     * Constructs an instance.
     *
     * @param dictionaryProvidersArg the dictionary providers
     * @param presenterArg           the presenter
     */
    public DictionaryServiceImpl(final Collection<DictionaryProvider> dictionaryProvidersArg,
                                 final Presenter presenterArg) {
        listDictionaryProvidersUsecase =
                new ListDictionaryProvidersUsecase(dictionaryProvidersArg, presenterArg);
        listDictionariesUsecase = new ListDictionariesUsecase(dictionaryProvidersArg, presenterArg);
        listDictionaryEntries = new ListDictionaryEntriesUsecase(dictionaryProvidersArg,
                presenterArg);
        searchDictionaryEntries = new SearchDictionaryEntriesUsecase(dictionaryProvidersArg,
                presenterArg);
        showPreferredDictionaryUsecase =
                new ShowPreferredDictionaryUsecase(dictionaryProvidersArg, presenterArg);
    }

    @Override
    public void listProviders() {
        listDictionaryProvidersUsecase.process();
    }

    @Override
    public void listDictionaries(final ListDictionariesRequest request) {
        listDictionariesUsecase.process(request);
    }

    @Override
    public void listEntries(final ListDictionaryEntriesRequest request) {
        listDictionaryEntries.process(request);
    }

    @Override
    public void searchEntries(final SearchDictionaryEntriesRequest request) {
        searchDictionaryEntries.process(request);
    }

    @Override
    public void showPreferredDictionary() {
        showPreferredDictionaryUsecase.process();
    }
}
