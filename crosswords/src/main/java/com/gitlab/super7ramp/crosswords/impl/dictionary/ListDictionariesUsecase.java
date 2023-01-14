/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.impl.dictionary;

import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionariesRequest;
import com.gitlab.super7ramp.crosswords.common.dictionary.ProvidedDictionaryDescription;
import com.gitlab.super7ramp.crosswords.impl.common.DictionarySelection;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.presenter.dictionary.DictionaryPresenter;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * The 'list dictionaries' use case.
 */
final class ListDictionariesUsecase {

    /** The criteria used to compare dictionaries. */
    private static final Comparator<ProvidedDictionaryDescription> DICTIONARY_COMPARATOR =
            new DictionaryComparator();

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
    ListDictionariesUsecase(final Collection<DictionaryProvider> dictionaryProvidersArg,
                            final DictionaryPresenter presenterArg) {
        dictionaryProviders = dictionaryProvidersArg;
        presenter = presenterArg;
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
            final List<ProvidedDictionaryDescription> dictionaries =
                    orderDictionaries(selectedDictionaryProviders);
            presenter.presentDictionaries(dictionaries);
        }
    }

    /**
     * Streams given dictionary provider collection and map item to
     * {@link ProvidedDictionaryDescription}.
     *
     * @param provider the dictionary provider
     * @return a stream of {@link ProvidedDictionaryDescription}s
     */
    private static Stream<ProvidedDictionaryDescription> toDictionaryDescriptionStream(final DictionaryProvider provider) {
        return provider.get()
                       .stream()
                       .map(dictionary -> new ProvidedDictionaryDescription(provider.description(), dictionary.description()));
    }

    /**
     * Returns a list of the given selected dictionary providers ordered by
     * {@link #DICTIONARY_COMPARATOR}.
     *
     * @param selectedDictionaryProviders the selected dictionary providers
     * @return a list of the given selected dictionary providers ordered by
     * {@link #DICTIONARY_COMPARATOR}
     */
    private static List<ProvidedDictionaryDescription> orderDictionaries(final Collection<DictionaryProvider> selectedDictionaryProviders) {
        return selectedDictionaryProviders.stream()
                                          .flatMap(provider -> toDictionaryDescriptionStream(provider))
                                          .sorted(DICTIONARY_COMPARATOR)
                                          .toList();
    }

    /**
     * Selects the dictionary providers from the given {@link ListDictionariesRequest}.
     *
     * @param request the {@link ListDictionariesRequest} to use as filter
     * @return the selected dictionary providers
     */
    private Collection<DictionaryProvider> selectDictionaryProviders(final ListDictionariesRequest request) {
        return DictionarySelection.byOptionalProvider(request.provider())
                                  .and(DictionarySelection.byOptionalLocale(request.locale()))
                                  .apply(dictionaryProviders);
    }
}
