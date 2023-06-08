/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.dictionary;

import com.gitlab.super7ramp.croiseur.impl.dictionary.error.DictionaryErrorMessages;
import com.gitlab.super7ramp.croiseur.impl.dictionary.selection.DictionarySelector;
import com.gitlab.super7ramp.croiseur.impl.dictionary.selection.SelectedDictionary;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryPresenter;

import java.util.Optional;

/**
 * The 'get default dictionary' usecase.
 */
final class GetDefaultDictionaryUsecase {

    /** The dictionary selector. */
    private final DictionarySelector dictionarySelector;

    /** The presenter. */
    private final DictionaryPresenter presenter;

    /**
     * Constructs an instance.
     *
     * @param dictionarySelectorArg the dictionary selector
     * @param presenterArg          the presenter
     */
    GetDefaultDictionaryUsecase(final DictionarySelector dictionarySelectorArg,
                                final DictionaryPresenter presenterArg) {
        dictionarySelector = dictionarySelectorArg;
        presenter = presenterArg;
    }

    /**
     * Processes the 'get default dictionary' event.
     */
    void process() {
        final Optional<SelectedDictionary> selectedDictionary = dictionarySelector.selectDefault();
        if (selectedDictionary.isEmpty()) {
            presenter.presentDictionaryError(DictionaryErrorMessages.NO_DICTIONARY_ERROR_MESSAGE);
        } else {
            presenter.presentDefaultDictionary(selectedDictionary.get().details());
        }
    }
}
