/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.dictionary;

import com.gitlab.super7ramp.croiseur.common.dictionary.ProvidedDictionaryDetails;
import com.gitlab.super7ramp.croiseur.impl.common.DictionarySelection;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryPresenter;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The 'show preferred dictionary' usecase.
 */
// FIXME some code could be shared with ListDictionariesUsecase
final class ShowPreferredDictionaryUsecase {

    /** The criteria used to compare dictionaries. */
    private static final Comparator<ProvidedDictionaryDetails> DICTIONARY_COMPARATOR =
            new DictionaryComparator();

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
    ShowPreferredDictionaryUsecase(final Collection<DictionaryProvider> dictionaryProvidersArg,
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

    void process() {
        final Collection<DictionaryProvider> filteredDictionaryProviders =
                DictionarySelection.any().apply(dictionaryProviders);

        if (filteredDictionaryProviders.isEmpty()) {
            presenter.presentDictionaryError(DictionaryErrorMessages.NO_DICTIONARY_ERROR_MESSAGE);
        } else {

            final Optional<ProvidedDictionaryDetails> optPreferredDictionary =
                    dictionaryProviders.stream()
                                       .flatMap(provider -> toDictionaryDetailsStream(provider))
                                       .min(DICTIONARY_COMPARATOR);

            if (optPreferredDictionary.isEmpty()) {
                presenter.presentDictionaryError(
                        DictionaryErrorMessages.NO_DICTIONARY_ERROR_MESSAGE);
            } else {
                presenter.presentPreferredDictionary(optPreferredDictionary.get());
            }
        }
    }
}
