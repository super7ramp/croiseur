/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.dictionary;

import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryPresenter;

import java.util.Collection;
import java.util.List;

/**
 * Lists the dictionary providers.
 */
final class ListDictionaryProvidersUsecase {

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
    ListDictionaryProvidersUsecase(final Collection<DictionaryProvider> dictionaryProvidersArg,
                                   final DictionaryPresenter presenterArg) {
        dictionaryProviders = dictionaryProvidersArg;
        presenter = presenterArg;
    }

    /**
     * Processes the 'list dictionary providers' event.
     */
    void process() {
        if (dictionaryProviders.isEmpty()) {
            presenter.presentDictionaryError(DictionaryErrorMessages.NO_DICTIONARY_ERROR_MESSAGE);
        } else {
            final List<DictionaryProviderDescription> presentableProviders =
                    dictionaryProviders.stream()
                                       .map(DictionaryProvider::description)
                                       .toList();
            presenter.presentDictionaryProviders(presentableProviders);
        }
    }
}
