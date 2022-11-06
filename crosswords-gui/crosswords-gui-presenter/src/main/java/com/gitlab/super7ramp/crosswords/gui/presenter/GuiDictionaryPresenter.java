package com.gitlab.super7ramp.crosswords.gui.presenter;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryDescription;
import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.crosswords.gui.view.model.DictionaryListViewEntry;
import com.gitlab.super7ramp.crosswords.gui.view.model.DictionaryViewModel;
import com.gitlab.super7ramp.crosswords.spi.presenter.dictionary.DictionaryPresenter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

final class GuiDictionaryPresenter implements DictionaryPresenter {

    /** The view model. */
    private final DictionaryViewModel dictionaryViewModel;

    /**
     * Constructs an instance.
     */
    GuiDictionaryPresenter(final DictionaryViewModel dictionaryViewModelArg) {
        dictionaryViewModel = dictionaryViewModelArg;
    }

    @Override
    public void presentDictionaryProviders(final Collection<DictionaryProviderDescription> providers) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentDictionaries(final Map<DictionaryProviderDescription, Collection<?
            extends DictionaryDescription>> dictionariesPerProviders) {

        final Collection<DictionaryListViewEntry> presentedDictionaries =
                dictionariesPerProviders.entrySet().stream().flatMap(
                        entry -> {
                            final String provider = entry.getKey().name();
                            final Collection<? extends DictionaryDescription> dictionaries =
                                    entry.getValue();
                            return dictionaries.stream()
                                               .map(dictionary -> new DictionaryListViewEntry(dictionary.locale(),
                                                       provider, dictionary.name()));
                        }
                ).toList();
        dictionaryViewModel.dictionariesProperty().setAll(presentedDictionaries);
    }

    @Override
    public void presentDictionaryEntries(final List<String> entries) {
        dictionaryViewModel.dictionaryEntries().setAll(entries);
    }

    @Override
    public void presentPreferredDictionary(final DictionaryDescription preferredDictionary) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
