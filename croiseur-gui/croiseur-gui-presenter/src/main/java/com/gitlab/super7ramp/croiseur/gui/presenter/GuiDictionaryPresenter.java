/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.presenter;

import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryProviderDetails;
import com.gitlab.super7ramp.croiseur.common.dictionary.ProvidedDictionaryDetails;
import com.gitlab.super7ramp.croiseur.gui.view.model.dictionary.DictionariesViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.dictionary.DictionaryKey;
import com.gitlab.super7ramp.croiseur.gui.view.model.dictionary.DictionaryViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.error.ErrorsViewModel;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryContent;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryPresenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionarySearchResult;
import javafx.application.Platform;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * GUI implementation of {@link DictionaryPresenter}.
 */
final class GuiDictionaryPresenter implements DictionaryPresenter {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(GuiDictionaryPresenter.class.getName());

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
        throw new UnsupportedOperationException("Not used");
    }

    @Override
    public void presentDictionaries(final List<ProvidedDictionaryDetails> providedDictionaries) {
        LOGGER.info(() -> "Received dictionaries: " + providedDictionaries);
        final List<DictionaryViewModel> presentedDictionaries =
                providedDictionaries.stream().map(GuiDictionaryPresenter::dictionaryViewModelFrom)
                                    .toList();
        // The first dictionary is the default one, automatically select it
        if (!presentedDictionaries.isEmpty()) {
            presentedDictionaries.get(0).select();
        }
        Platform.runLater(() -> dictionariesViewModel.dictionariesProperty()
                                                     .setAll(presentedDictionaries));
    }

    @Override
    public void presentDictionaryEntries(final DictionaryContent content) {
        LOGGER.info(() -> "Received entries of dictionary " + content.details() + ": " +
                          content.words().size() + " words");
        Platform.runLater(() -> dictionariesViewModel.addWords(dictionaryKeyFrom(content.details()),
                                                               content.words()));
    }

    @Override
    public void presentDictionarySearchResult(final DictionarySearchResult searchResult) {
        throw new UnsupportedOperationException("Not used");
    }

    @Override
    public void presentDefaultDictionary(final ProvidedDictionaryDetails defaultDictionary) {
        throw new UnsupportedOperationException("Not used");
    }

    @Override
    public void presentDictionaryError(final String error) {
        LOGGER.warning(() -> "Received dictionary error: " + error);
        Platform.runLater(() -> errorsViewModel.addError(error));
    }

    private static DictionaryViewModel dictionaryViewModelFrom(
            final ProvidedDictionaryDetails details) {
        return new DictionaryViewModel(details.providerName(), details.dictionaryName(),
                                       details.dictionaryLocale(), details.dictionaryDescription());
    }

    private static DictionaryKey dictionaryKeyFrom(final ProvidedDictionaryDetails details) {
        return new DictionaryKey(details.providerName(), details.dictionaryName(),
                                 details.dictionaryLocale());
    }
}
