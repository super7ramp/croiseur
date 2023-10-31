/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.dictionary;

import re.belv.croiseur.common.dictionary.DictionaryProviderDetails;
import re.belv.croiseur.impl.dictionary.error.DictionaryErrorMessages;
import re.belv.croiseur.spi.dictionary.DictionaryProvider;
import re.belv.croiseur.spi.presenter.dictionary.DictionaryPresenter;

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
            final List<DictionaryProviderDetails> presentableProviders =
                    dictionaryProviders.stream()
                                       .map(DictionaryProvider::details)
                                       .toList();
            presenter.presentDictionaryProviders(presentableProviders);
        }
    }
}
