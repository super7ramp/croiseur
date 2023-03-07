/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.presenter;

import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.croiseur.common.dictionary.ProvidedDictionaryDescription;
import com.gitlab.super7ramp.croiseur.gui.view.model.DictionariesViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.DictionaryViewModel;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryContent;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryPresenter;
import javafx.application.Platform;

import java.util.Collection;
import java.util.List;

final class GuiDictionaryPresenter implements DictionaryPresenter {

    /** The view model. */
    private final DictionariesViewModel dictionariesViewModel;

    /**
     * Constructs an instance.
     */
    GuiDictionaryPresenter(final DictionariesViewModel dictionariesViewModelArg) {
        dictionariesViewModel = dictionariesViewModelArg;
    }

    @Override
    public void presentDictionaryProviders(final Collection<DictionaryProviderDescription> providers) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentDictionaries(final List<ProvidedDictionaryDescription> providedDictionaries) {
        final List<DictionaryViewModel> presentedDictionaries =
                providedDictionaries.stream().map(DictionaryConverter::toViewModelType).toList();
        // The first dictionary is the default one, automatically select it
        if (!presentedDictionaries.isEmpty()) {
            presentedDictionaries.get(0).setSelected(true);
        }
        Platform.runLater(() -> dictionariesViewModel.dictionariesProperty()
                                                     .setAll(presentedDictionaries));
    }

    @Override
    public void presentDictionaryEntries(final DictionaryContent content) {
        Platform.runLater(() -> dictionariesViewModel.addWords(content.description()
                                                                      .toDictionaryKey(),
                content.words()));
    }

    @Override
    public void presentPreferredDictionary(final ProvidedDictionaryDescription preferredDictionary) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentDictionaryError(final String error) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
