package com.gitlab.super7ramp.crosswords.impl.dictionary;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryUsecase;
import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionariesRequest;
import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionaryEntriesRequest;
import com.gitlab.super7ramp.crosswords.common.dictionary.ProvidedDictionaryDescription;
import com.gitlab.super7ramp.crosswords.impl.common.DictionarySelection;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.presenter.Presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Implementation of {@link DictionaryUsecase}.
 */
public final class DictionaryUsecaseImpl implements DictionaryUsecase {

    /** Error message to publish when no dictionary matching the request is found. */
    private static final String NO_DICTIONARY_ERROR_MESSAGE = "No dictionary found";

    /** Error message to publish when an ambiguous request is received. */
    private static final String AMBIGUOUS_REQUEST_ERROR_MESSAGE = "Ambiguous request: Found " +
            "matching dictionaries for several providers";

    /** The criteria used to compare dictionaries. */
    private static final Comparator<ProvidedDictionaryDescription> DICTIONARY_COMPARATOR =
            new DictionaryComparator();

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
            presenter.presentDictionaryProviders(dictionaryProviders.stream()
                                                                    .map(DictionaryProvider::description)
                                                                    .toList());
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

            final List<ProvidedDictionaryDescription> dictionaries =
                    dictionaryProviders.stream().flatMap(
                            provider -> provider.get()
                                                .stream()
                                                .map(dictionary -> new ProvidedDictionaryDescription(provider.description(), dictionary.description()))
                    ).sorted(DICTIONARY_COMPARATOR).toList();

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
                                      .ifPresentOrElse(dictionary -> presenter.presentDictionaryEntries(dictionary.stream()
                                                                                                                  .sorted()
                                                                                                                  .toList()),
                                              () -> presenter.presentError(NO_DICTIONARY_ERROR_MESSAGE));
        }
    }

    @Override
    public void showPreferredDictionary() {
        final Collection<DictionaryProvider> filteredDictionaryProviders =
                DictionarySelection.any().apply(dictionaryProviders);

        if (filteredDictionaryProviders.isEmpty()) {
            presenter.presentError(NO_DICTIONARY_ERROR_MESSAGE);
        } else {

            final ProvidedDictionaryDescription preferredDictionary =
                    dictionaryProviders.stream().flatMap(
                            provider -> provider.get()
                                                .stream()
                                                .map(dictionary -> new ProvidedDictionaryDescription(provider.description(), dictionary.description()))
                    ).min(DICTIONARY_COMPARATOR).orElseThrow();

            presenter.presentPreferredDictionary(preferredDictionary);
        }
    }
}
