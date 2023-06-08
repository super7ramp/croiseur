/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.dictionary;

import com.gitlab.super7ramp.croiseur.api.dictionary.ListDictionariesRequest;
import com.gitlab.super7ramp.croiseur.common.dictionary.ProvidedDictionaryDetails;
import com.gitlab.super7ramp.croiseur.impl.dictionary.error.DictionaryErrorMessages;
import com.gitlab.super7ramp.croiseur.impl.dictionary.selection.DictionaryComparator;
import com.gitlab.super7ramp.croiseur.impl.dictionary.selection.DictionaryProviderFilter;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryPresenter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * The 'list dictionaries' use case.
 */
final class ListDictionariesUsecase {

    /** The dictionary providers. */
    private final Collection<DictionaryProvider> dictionaryProviders;

    /** The presenter. */
    private final DictionaryPresenter presenter;

    /**
     * Constructs an instance.
     *
     * @param dictionaryProvidersArg the dictionary providers
     * @param presenterArg           the presenter
     */
    ListDictionariesUsecase(final Collection<DictionaryProvider> dictionaryProvidersArg,
                            final DictionaryPresenter presenterArg) {
        dictionaryProviders = dictionaryProvidersArg;
        presenter = presenterArg;
    }

    /**
     * Streams given dictionary provider collection and map item to
     * {@link ProvidedDictionaryDetails}.
     *
     * @param provider the dictionary provider
     * @return a stream of {@link ProvidedDictionaryDetails}s
     */
    private static Stream<ProvidedDictionaryDetails> toDictionaryDetailsStream(
            final DictionaryProvider provider) {
        return provider.get()
                       .stream()
                       .map(dictionary -> new ProvidedDictionaryDetails(
                               provider.details().name(), dictionary.details()));
    }

    /**
     * Returns a list of the given selected dictionary providers ordered by
     * {@link DictionaryComparator}.
     *
     * @param selectedDictionaryProviders the selected dictionary providers
     * @return a list of the given selected dictionary providers ordered by
     * {@link DictionaryComparator}
     */
    private static List<ProvidedDictionaryDetails> orderDictionaries(
            final Collection<DictionaryProvider> selectedDictionaryProviders) {
        return selectedDictionaryProviders.stream()
                                          .flatMap(
                                                  ListDictionariesUsecase::toDictionaryDetailsStream)
                                          .sorted(new DictionaryComparator())
                                          .toList();
    }

    /**
     * Processes the {@link ListDictionariesRequest}.
     *
     * @param request the event to process
     */
    void process(final ListDictionariesRequest request) {
        final Collection<DictionaryProvider> selectedDictionaryProviders =
                selectDictionaryProviders(request);

        if (selectedDictionaryProviders.isEmpty()) {
            presenter.presentDictionaryError(DictionaryErrorMessages.NO_DICTIONARY_ERROR_MESSAGE);
        } else {
            final List<ProvidedDictionaryDetails> dictionaries =
                    orderDictionaries(selectedDictionaryProviders);
            presenter.presentDictionaries(dictionaries);
        }
    }

    /**
     * Selects the dictionary providers from the given {@link ListDictionariesRequest}.
     *
     * @param request the {@link ListDictionariesRequest} to use as filter
     * @return the selected dictionary providers
     */
    private Collection<DictionaryProvider> selectDictionaryProviders(
            final ListDictionariesRequest request) {
        return DictionaryProviderFilter.byOptionalProvider(request.provider())
                                       .and(DictionaryProviderFilter.byOptionalLocale(
                                               request.locale()))
                                       .apply(dictionaryProviders);
    }
}
