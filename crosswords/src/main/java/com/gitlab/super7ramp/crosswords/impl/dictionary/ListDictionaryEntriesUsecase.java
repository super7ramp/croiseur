/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.impl.dictionary;

import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionaryEntriesRequest;
import com.gitlab.super7ramp.crosswords.common.dictionary.ProvidedDictionaryDescription;
import com.gitlab.super7ramp.crosswords.impl.common.DictionarySelection;
import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.presenter.dictionary.DictionaryContent;
import com.gitlab.super7ramp.crosswords.spi.presenter.dictionary.DictionaryPresenter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Lists the dictionary entries.
 */
final class ListDictionaryEntriesUsecase {

    /** The dictionary providers. */
    private final Collection<DictionaryProvider> dictionaryProviders;

    /** The presenter. */
    private final DictionaryPresenter presenter;

    /**
     * Constructs an instance.
     *
     * @param dictionaryProvidersArg the dictionary providers
     * @param presenterArg the presenter
     */
    ListDictionaryEntriesUsecase(Collection<DictionaryProvider> dictionaryProvidersArg,
                                        DictionaryPresenter presenterArg) {
        dictionaryProviders = dictionaryProvidersArg;
        presenter = presenterArg;
    }

    /**
     * Processes the given {@link ListDictionaryEntriesRequest}.
     *
     * @param request the {@link ListDictionaryEntriesRequest}
     */
    void process(final ListDictionaryEntriesRequest request) {
        final Collection<DictionaryProvider> selectedDictionaryProviders =
                selectDictionaryProviders(request);

        if (selectedDictionaryProviders.isEmpty()) {
            presenter.presentDictionaryError(DictionaryErrorMessages.NO_DICTIONARY_ERROR_MESSAGE);
            return;
        }
        if (selectedDictionaryProviders.size() > 1) {
            presenter.presentDictionaryError(DictionaryErrorMessages.AMBIGUOUS_REQUEST_ERROR_MESSAGE + " (" + selectedDictionaryProviders + ")");
            return;
        }

        final DictionaryProvider selectedDictionaryProvider = selectedDictionaryProviders.iterator().next();
        final Optional<Dictionary> optFirstDictionary = selectedDictionaryProvider.getFirst();
        if (optFirstDictionary.isEmpty()) {
            presenter.presentDictionaryError(DictionaryErrorMessages.NO_DICTIONARY_ERROR_MESSAGE);
            return;
        }

        final Dictionary dictionary = optFirstDictionary.get();
        final DictionaryContent dictionaryContent =
                readContent(selectedDictionaryProvider, dictionary);
        presenter.presentDictionaryEntries(dictionaryContent);
    }

    /**
     * Reads the content of the given dictionary into a {@link DictionaryContent}.
     *
     * @param selectedDictionaryProvider the selected dictionary provider
     * @param dictionary the selected dictionary
     * @return the content of the given dictionary
     */
    private static DictionaryContent readContent(final DictionaryProvider selectedDictionaryProvider, final Dictionary dictionary) {
        final ProvidedDictionaryDescription description =
                new ProvidedDictionaryDescription(selectedDictionaryProvider.description(), dictionary.description());
        final List<String> words = dictionary.stream().toList();
        return new DictionaryContent(description, words);
    }

    /**
     * Selects the dictionary providers using the given request as filer.
     *
     * @param request the request to use as filter
     * @return the selected dictionary providers
     */
    private Collection<DictionaryProvider> selectDictionaryProviders(ListDictionaryEntriesRequest request) {
        return DictionarySelection.byId(request.dictionaryIdentifier())
                                  .apply(dictionaryProviders);
    }
}
