/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.presenter;

import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryProviderDetails;
import com.gitlab.super7ramp.croiseur.common.dictionary.ProvidedDictionaryDetails;
import com.gitlab.super7ramp.croiseur.gui.view.model.DictionariesViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.DictionaryViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.ErrorsViewModel;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryContent;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryPresenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionarySearchResult;
import javafx.application.Platform;

import java.util.Collection;
import java.util.List;

/**
 * GUI implementation of {@link DictionaryPresenter}.
 */
final class GuiDictionaryPresenter implements DictionaryPresenter {

    /** The view model. */
    private final DictionariesViewModel dictionariesViewModel;

    /** The errors view model. */
    private final ErrorsViewModel errorsViewModel;

    /**
     * Constructs an instance.
     *
     * @param dictionariesViewModelArg the dictionaries view model
     * @param errorsViewModelArg       the errors view model
     */
    GuiDictionaryPresenter(final DictionariesViewModel dictionariesViewModelArg,
                           final ErrorsViewModel errorsViewModelArg) {
        dictionariesViewModel = dictionariesViewModelArg;
        errorsViewModel = errorsViewModelArg;
    }

    @Override
    public void presentDictionaryProviders(final Collection<DictionaryProviderDetails> providers) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentDictionaries(final List<ProvidedDictionaryDetails> providedDictionaries) {
        final List<DictionaryViewModel> presentedDictionaries =
                providedDictionaries.stream().map(DictionaryViewModel::new).toList();
        // The first dictionary is the default one, automatically select it
        if (!presentedDictionaries.isEmpty()) {
            presentedDictionaries.get(0).setSelected(true);
        }
        Platform.runLater(() -> dictionariesViewModel.dictionariesProperty()
                                                     .setAll(presentedDictionaries));
    }

    @Override
    public void presentDictionaryEntries(final DictionaryContent content) {
        Platform.runLater(() -> dictionariesViewModel.addWords(content.details()
                                                                      .toDictionaryKey(),
                                                               content.words()));
    }

    @Override
    public void presentDictionarySearchResult(final DictionarySearchResult searchResult) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentPreferredDictionary(final ProvidedDictionaryDetails preferredDictionary) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentDictionaryError(final String error) {
        Platform.runLater(() -> errorsViewModel.addError(error));
    }
}
