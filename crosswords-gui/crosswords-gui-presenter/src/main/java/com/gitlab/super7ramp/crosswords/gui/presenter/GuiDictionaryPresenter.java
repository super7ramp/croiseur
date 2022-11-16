package com.gitlab.super7ramp.crosswords.gui.presenter;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.crosswords.common.dictionary.ProvidedDictionaryDescription;
import com.gitlab.super7ramp.crosswords.gui.view.model.DictionaryListViewEntry;
import com.gitlab.super7ramp.crosswords.gui.view.model.DictionaryViewModel;
import com.gitlab.super7ramp.crosswords.spi.presenter.dictionary.DictionaryPresenter;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

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
    public void presentDictionaries(final List<ProvidedDictionaryDescription> providedDictionaries) {
        final List<DictionaryListViewEntry> presentedDictionaries =
                providedDictionaries.stream().map(
                        providedDictionary -> {
                            final String provider = providedDictionary.provider().name();
                            final Locale locale = providedDictionary.dictionary().locale();
                            final String dictionaryName = providedDictionary.dictionary().name();
                            return new DictionaryListViewEntry(locale, provider, dictionaryName);
                        }
                ).toList();
        // The first dictionary is the default one, automatically select it
        if (!presentedDictionaries.isEmpty()) {
            presentedDictionaries.get(0).selectedProperty().set(true);
        }
        dictionaryViewModel.dictionariesProperty().setAll(presentedDictionaries);
    }

    @Override
    public void presentDictionaryEntries(final List<String> entries) {
        dictionaryViewModel.dictionaryEntries().setAll(entries);
    }

    @Override
    public void presentPreferredDictionary(final ProvidedDictionaryDescription preferredDictionary) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
