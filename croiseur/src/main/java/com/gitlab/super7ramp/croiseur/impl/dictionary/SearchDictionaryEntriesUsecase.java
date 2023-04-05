/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.dictionary;

import com.gitlab.super7ramp.croiseur.api.dictionary.SearchDictionaryEntriesRequest;
import com.gitlab.super7ramp.croiseur.impl.common.DictionarySelection;
import com.gitlab.super7ramp.croiseur.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryPresenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionarySearchResult;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Searches dictionary entries.
 */
final class SearchDictionaryEntriesUsecase {

    /** The dictionary providers. */
    private final Collection<DictionaryProvider> dictionaryProviders;

    /** The dictionary presenter. */
    private final DictionaryPresenter presenter;

    /**
     * Constructs an instance.
     *
     * @param dictionaryProvidersArg the dictionary providers
     */
    SearchDictionaryEntriesUsecase(final Collection<DictionaryProvider> dictionaryProvidersArg,
                                   final DictionaryPresenter dictionaryPresenterArg) {
        dictionaryProviders = dictionaryProvidersArg;
        presenter = dictionaryPresenterArg;
    }

    /**
     * Processes the given {@link SearchDictionaryEntriesRequest}.
     *
     * @param request the request to process
     */
    void process(final SearchDictionaryEntriesRequest request) {

        // start dictionary search logic
        // TODO this is the same as "list entries usecase", find a clean way to share it
        final Collection<DictionaryProvider> selectedDictionaryProviders =
                DictionarySelection.byId(request.dictionaryIdentifier())
                .apply(dictionaryProviders);

        if (selectedDictionaryProviders.isEmpty()) {
            presenter.presentDictionaryError(DictionaryErrorMessages.NO_DICTIONARY_ERROR_MESSAGE);
            return;
        }
        if (selectedDictionaryProviders.size() > 1) {
            presenter.presentDictionaryError(DictionaryErrorMessages.AMBIGUOUS_REQUEST_ERROR_MESSAGE + " (" + selectedDictionaryProviders + ")");
            return;
        }

        final DictionaryProvider selectedDictionaryProvider = selectedDictionaryProviders.iterator()
                .next();
        final Optional<Dictionary> optFirstDictionary = selectedDictionaryProvider.getFirst();
        if (optFirstDictionary.isEmpty()) {
            presenter.presentDictionaryError(DictionaryErrorMessages.NO_DICTIONARY_ERROR_MESSAGE);
            return;
        }

        final Dictionary dictionary = optFirstDictionary.get();
        // end dictionary search logic

        final Predicate<String> regex = Pattern.compile(request.searchExpression())
                .asMatchPredicate();
        final List<String> foundWords = dictionary.words().stream().filter(regex).toList();
        final DictionarySearchResult searchResult = new DictionarySearchResult(foundWords);

        presenter.presentDictionarySearchResult(searchResult);
    }

}
