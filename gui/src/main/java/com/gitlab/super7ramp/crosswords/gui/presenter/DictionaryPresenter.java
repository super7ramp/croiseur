package com.gitlab.super7ramp.crosswords.gui.presenter;

import com.gitlab.super7ramp.crosswords.gui.viewmodel.DictionaryViewModel;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;

import java.util.Collection;
import java.util.logging.Logger;

final class DictionaryPresenter {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(DictionaryPresenter.class.getName());

    /** The view model. */
    private final DictionaryViewModel dictionaryViewModel;

    /**
     * Constructs an instance.
     */
    DictionaryPresenter(final DictionaryViewModel dictionaryViewModelArg) {
        dictionaryViewModel = dictionaryViewModelArg;
    }

    /**
     * Presents the given dictionary result.
     *
     * @param dictionaryProviders the dictionary result
     */
    void presentDictionaries(final Collection<DictionaryProvider> dictionaryProviders) {
        LOGGER.fine(() -> "Presenting dictionaries: " + dictionaryProviders);
        final Collection<String> dictionaries = dictionaryProviders.stream()
                                                                   .flatMap(dp -> dp.get()
                                                                                    .stream()
                                                                                    .map(d -> dp.name() + ":" + d.name()))
                                                                   .toList();
        dictionaryViewModel.dictionaries().setAll(dictionaries);
    }

}
