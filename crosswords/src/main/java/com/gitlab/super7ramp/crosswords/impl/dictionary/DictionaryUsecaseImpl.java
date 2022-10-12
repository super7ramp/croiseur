package com.gitlab.super7ramp.crosswords.impl.dictionary;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryUsecase;
import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionariesRequest;
import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionaryEntriesRequest;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionarySearch;
import com.gitlab.super7ramp.crosswords.spi.presenter.Presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Implementation of {@link DictionaryUsecase}.
 */
public final class DictionaryUsecaseImpl implements DictionaryUsecase {

    /** Error message to publish when no dictionary matching the request is found. */
    private static final String NO_DICTIONARY_ERROR_MESSAGE = "No dictionary found";

    /** Error message to publish when an ambiguous request is received. */
    private static final String AMBIGUOUS_REQUEST_ERROR_MESSAGE = "Ambiguous request: Found " +
            "matching dictionaries for several providers";

    /** The dictionary loader. */
    private final Collection<DictionaryProvider> dictionaryProviders;

    /** The publisher. */
    private final Presenter presenter;

    /**
     * Constructor.
     *
     * @param someDictionaryProviders some dictionary providers
     * @param aPresenter              a publisher
     */
    public DictionaryUsecaseImpl(final Collection<DictionaryProvider> someDictionaryProviders,
                                 final Presenter aPresenter) {
        dictionaryProviders = new ArrayList<>(someDictionaryProviders);
        presenter = aPresenter;
    }

    @Override
    public void listProviders() {
        if (dictionaryProviders.isEmpty()) {
            presenter.publishError(NO_DICTIONARY_ERROR_MESSAGE);
        } else {
            presenter.publishDictionaryProviders(Collections.unmodifiableCollection(dictionaryProviders));
        }
    }

    @Override
    public void listDictionaries(final ListDictionariesRequest request) {

        final Collection<DictionaryProvider> filteredDictionaryProviders =
                DictionarySearch.byOptionalProvider(request.provider())
                                .and(DictionarySearch.byOptionalLocale(request.locale()))
                                .apply(dictionaryProviders);

        if (filteredDictionaryProviders.isEmpty()) {
            presenter.publishError(NO_DICTIONARY_ERROR_MESSAGE);
        } else {
            presenter.publishDictionaries(filteredDictionaryProviders);
        }
    }

    @Override
    public void listEntries(final ListDictionaryEntriesRequest request) {

        final Collection<DictionaryProvider> filteredDictionaryProviders =
                DictionarySearch.byOptionalProvider(request.dictionaryProvider())
                                .and(DictionarySearch.byName(request.dictionaryName()))
                                .apply(dictionaryProviders);

        if (filteredDictionaryProviders.isEmpty()) {
            presenter.publishError(NO_DICTIONARY_ERROR_MESSAGE);
        } else if (filteredDictionaryProviders.size() > 1) {
            presenter.publishError(AMBIGUOUS_REQUEST_ERROR_MESSAGE + " (" + filteredDictionaryProviders + ")");
        } else {
            final DictionaryProvider filteredDictionaryProvider =
                    filteredDictionaryProviders.iterator().next();

            filteredDictionaryProvider.getFirst()
                                      .ifPresentOrElse(presenter::publishDictionaryEntries,
                                              () -> presenter.publishError(NO_DICTIONARY_ERROR_MESSAGE));
        }
    }
}
