package com.gitlab.super7ramp.crosswords.impl.dictionary;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryUsecase;
import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionariesRequest;
import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionaryEntriesRequest;
import com.gitlab.super7ramp.crosswords.impl.common.DictionarySelection;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryDescription;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.crosswords.spi.presenter.Presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

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
            presenter.presentError(NO_DICTIONARY_ERROR_MESSAGE);
        } else {
            presenter.presentDictionaryProviders(Collections.unmodifiableCollection(dictionaryProviders));
        }
    }

    @Override
    public void listDictionaries(final ListDictionariesRequest request) {

        final Collection<DictionaryProvider> filteredDictionaryProviders =
                DictionarySelection.byOptionalProvider(request.provider())
                                   .and(DictionarySelection.byOptionalLocale(request.locale()))
                                   .apply(dictionaryProviders);

        if (filteredDictionaryProviders.isEmpty()) {
            presenter.presentError(NO_DICTIONARY_ERROR_MESSAGE);
        } else {

            final Map<DictionaryProviderDescription, Collection<? extends DictionaryDescription>> dictionaries =
                    dictionaryProviders.stream()
                                       .collect(toMap(identity(), provider -> provider.get()));

            presenter.presentDictionaries(dictionaries);
        }
    }

    @Override
    public void listEntries(final ListDictionaryEntriesRequest request) {

        final Collection<DictionaryProvider> filteredDictionaryProviders =
                DictionarySelection.byId(request.dictionaryIdentifier())
                                   .apply(dictionaryProviders);

        if (filteredDictionaryProviders.isEmpty()) {
            presenter.presentError(NO_DICTIONARY_ERROR_MESSAGE);
        } else if (filteredDictionaryProviders.size() > 1) {
            presenter.presentError(AMBIGUOUS_REQUEST_ERROR_MESSAGE + " (" + filteredDictionaryProviders + ")");
        } else {
            final DictionaryProvider filteredDictionaryProvider =
                    filteredDictionaryProviders.iterator().next();

            filteredDictionaryProvider.getFirst()
                                      .ifPresentOrElse(presenter::presentDictionaryEntries,
                                              () -> presenter.presentError(NO_DICTIONARY_ERROR_MESSAGE));
        }
    }

    @Override
    public void showPreferredDictionary() {
        // TODO Use implementation from solver, make it common
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
