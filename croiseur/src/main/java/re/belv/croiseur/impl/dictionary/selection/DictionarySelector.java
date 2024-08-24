/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.dictionary.selection;

import static java.util.Comparator.comparing;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import re.belv.croiseur.api.dictionary.DictionaryIdentifier;
import re.belv.croiseur.common.util.Either;
import re.belv.croiseur.impl.dictionary.error.DictionaryErrorMessages;
import re.belv.croiseur.spi.dictionary.Dictionary;
import re.belv.croiseur.spi.dictionary.DictionaryProvider;

/** Select dictionaries. */
public final class DictionarySelector {

    /** The dictionary providers. */
    private final Collection<DictionaryProvider> dictionaryProviders;

    /**
     * Constructs an instance.
     *
     * @param dictionaryProvidersArg the dictionary providers
     */
    public DictionarySelector(final Collection<DictionaryProvider> dictionaryProvidersArg) {
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
     * @param dictionary the selected dictionary
     * @return a success result wrapping the selected dictionary
     */
    private static Either<String, SelectedDictionary> success(
            final DictionaryProvider dictionaryProvider, final Dictionary dictionary) {
        final var selectedDictionary =
                SelectedDictionary.of(dictionaryProvider.details().name(), dictionary);
        return Either.rightOf(selectedDictionary);
    }

    /**
     * Extracts the dictionaries of given dictionary provider into a stream of {@link SelectedDictionary}.
     *
     * @param p the dictionary provider
     * @return the dictionaries
     */
    private static Stream<SelectedDictionary> dictionariesOf(final DictionaryProvider p) {
        return p.get().stream().map(d -> SelectedDictionary.of(p.details().name(), d));
    }

    /**
     * Selects a unique dictionary given a dictionary identifier.
     *
     * @param dictionaryIdentifier the dictionary identifier
     * @return either a unique matching dictionary or an error message
     */
    public Either<String, SelectedDictionary> select(final DictionaryIdentifier dictionaryIdentifier) {

        final Collection<DictionaryProvider> selectedDictionaryProviders =
                DictionaryProviderFilter.byId(dictionaryIdentifier).apply(dictionaryProviders);

        final Either<String, SelectedDictionary> result;
        if (selectedDictionaryProviders.isEmpty()) {
            result = noDictionaryError();
        } else if (selectedDictionaryProviders.size() > 1) {
            result = ambiguousRequestError(selectedDictionaryProviders);
        } else {

            final DictionaryProvider dictionaryProvider =
                    selectedDictionaryProviders.iterator().next();
            final Optional<Dictionary> dictionaryOpt = dictionaryProvider.getFirst();

            result = dictionaryOpt
                    .map(dictionary -> success(dictionaryProvider, dictionary))
                    .orElseGet(DictionarySelector::noDictionaryError);
        }

        return result;
    }

    /**
     * Selects dictionaries with given IDs.
     *
     * @param ids the dictionary IDs
     * @return dictionaries with given IDs
     */
    public List<SelectedDictionary> select(final Collection<DictionaryIdentifier> ids) {
        final DictionaryProviderFilter selection = ids.stream()
                .map(DictionaryProviderFilter::byId)
                .reduce(DictionaryProviderFilter.none(), DictionaryProviderFilter::or);
        final Collection<DictionaryProvider> selectedDictionaryProviders = selection.apply(dictionaryProviders);
        return selectedDictionaryProviders.stream()
                .flatMap(DictionarySelector::dictionariesOf)
                .toList();
    }

    /**
     * Selects the default dictionary according to {@link DictionaryComparator}.
     *
     * @return the first dictionary
     */
    public Optional<SelectedDictionary> selectDefault() {
        return dictionaryProviders.stream()
                .flatMap(DictionarySelector::dictionariesOf)
                .min(comparing(SelectedDictionary::details, new DictionaryComparator()));
    }
}
