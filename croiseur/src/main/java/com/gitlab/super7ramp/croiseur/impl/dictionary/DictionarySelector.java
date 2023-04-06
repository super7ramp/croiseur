/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.dictionary;

import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryDescription;
import com.gitlab.super7ramp.croiseur.common.util.Either;
import com.gitlab.super7ramp.croiseur.impl.common.DictionarySelection;
import com.gitlab.super7ramp.croiseur.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * The dictionary selection logic shared by use-cases performing actions on a unique dictionary.
 */
final class DictionarySelector {

    /**
     * The dictionary selection logic result.
     *
     * @param providerName the name of the provider of the selected dictionary
     * @param description  the selected dictionary description
     * @param words        the selected dictionary words
     */
    record SelectedDictionary(String providerName, DictionaryDescription description,
            Set<String> words) {
        // Nothing to add.
    }

    /** The dictionary providers. */
    private final Collection<DictionaryProvider> dictionaryProviders;

    /**
     * Constructs an instance.
     *
     * @param dictionaryProvidersArg the dictionary providers
     */
    DictionarySelector(final Collection<DictionaryProvider> dictionaryProvidersArg) {
        dictionaryProviders = dictionaryProvidersArg;
    }

    /**
     * Returns an error result suitable when no matching dictionary is found.
     *
     * @return an error result suitable when no matching dictionary is found
     */
    private static Either<String, SelectedDictionary> noDictionaryError() {
        return Either.leftOf(DictionaryErrorMessages.NO_DICTIONARY_ERROR_MESSAGE);
    }

    /**
     * Returns an error result suitable when several dictionaries are matching.
     *
     * @return an error result suitable when several dictionaries are matching
     */
    private static Either<String, SelectedDictionary> ambiguousRequestError(
            final Collection<DictionaryProvider> selectedDictionaryProviders) {
        final String errorMessage =
                DictionaryErrorMessages.AMBIGUOUS_REQUEST_ERROR_MESSAGE + " (" + selectedDictionaryProviders + ")";
        return Either.leftOf(errorMessage);
    }

    /**
     * Returns a success result wrapping the selected dictionary.
     *
     * @param dictionaryProvider the selected dictionary provider
     * @param dictionary         the selected dictionary
     * @return a success result wrapping the selected dictionary
     */
    private static Either<String, SelectedDictionary> success(
            final DictionaryProvider dictionaryProvider, final Dictionary dictionary) {
        final var selectedDictionary =
                new SelectedDictionary(dictionaryProvider.description().name(),
                        dictionary.description(), dictionary.words());
        return Either.rightOf(selectedDictionary);
    }

    /**
     * Selects a unique dictionary given a dictionary identifier.
     *
     * @param dictionaryIdentifier the dictionary identifier
     * @return either a unique matching dictionary or an error message
     */
    Either<String, SelectedDictionary> select(final DictionaryIdentifier dictionaryIdentifier) {

        final Collection<DictionaryProvider> selectedDictionaryProviders =
                DictionarySelection.byId(dictionaryIdentifier).apply(dictionaryProviders);

        final Either<String, SelectedDictionary> result;
        if (selectedDictionaryProviders.isEmpty()) {
            result = noDictionaryError();
        } else if (selectedDictionaryProviders.size() > 1) {
            result = ambiguousRequestError(selectedDictionaryProviders);
        } else {

            final DictionaryProvider dictionaryProvider =
                    selectedDictionaryProviders.iterator().next();
            final Optional<Dictionary> dictionaryOpt = dictionaryProvider.getFirst();

            result = dictionaryOpt.map(dictionary -> success(dictionaryProvider, dictionary))
                                  .orElseGet(DictionarySelector::noDictionaryError);

        }

        return result;
    }
}
