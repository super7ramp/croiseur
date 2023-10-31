/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.dictionary;

import re.belv.croiseur.api.dictionary.DictionaryService;
import re.belv.croiseur.api.dictionary.ListDictionariesRequest;
import re.belv.croiseur.api.dictionary.ListDictionaryEntriesRequest;
import re.belv.croiseur.api.dictionary.SearchDictionaryEntriesRequest;
import re.belv.croiseur.impl.dictionary.selection.DictionarySelector;
import re.belv.croiseur.spi.dictionary.DictionaryProvider;
import re.belv.croiseur.spi.presenter.Presenter;

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
    private final GetDefaultDictionaryUsecase getDefaultDictionaryUsecase;

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
        final var dictionarySelector = new DictionarySelector(dictionaryProvidersArg);
        listDictionaryEntries = new ListDictionaryEntriesUsecase(dictionarySelector, presenterArg);
        searchDictionaryEntries =
                new SearchDictionaryEntriesUsecase(dictionarySelector, presenterArg);
        getDefaultDictionaryUsecase =
                new GetDefaultDictionaryUsecase(dictionarySelector, presenterArg);
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
    public void getDefaultDictionary() {
        getDefaultDictionaryUsecase.process();
    }
}
