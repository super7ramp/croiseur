/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.dictionary;

import re.belv.croiseur.api.dictionary.SearchDictionaryEntriesRequest;
import re.belv.croiseur.common.util.Either;
import re.belv.croiseur.impl.dictionary.selection.DictionarySelector;
import re.belv.croiseur.impl.dictionary.selection.SelectedDictionary;
import re.belv.croiseur.spi.presenter.dictionary.DictionaryPresenter;
import re.belv.croiseur.spi.presenter.dictionary.DictionarySearchResult;

import java.util.regex.Pattern;

/**
 * Searches dictionary entries.
 */
final class SearchDictionaryEntriesUsecase {

    /** The dictionary selector. */
    private final DictionarySelector dictionarySelector;

    /** The dictionary presenter. */
    private final DictionaryPresenter presenter;

    /**
     * Constructs an instance.
     *
     * @param dictionarySelectorArg  the dictionary selector
     * @param dictionaryPresenterArg the dictionary presenter
     */
    SearchDictionaryEntriesUsecase(final DictionarySelector dictionarySelectorArg,
                                   final DictionaryPresenter dictionaryPresenterArg) {
        dictionarySelector = dictionarySelectorArg;
        presenter = dictionaryPresenterArg;
    }

    /**
     * Processes the given {@link SearchDictionaryEntriesRequest}.
     *
     * @param request the request to process
     */
    void process(final SearchDictionaryEntriesRequest request) {

        final Either<String, SelectedDictionary> dictionarySelection =
                dictionarySelector.select(request.dictionaryIdentifier());

        if (dictionarySelection.isLeft()) {
            presenter.presentDictionaryError(dictionarySelection.left());
        } else {
            final SelectedDictionary selectedDictionary = dictionarySelection.right();
            final var regex = Pattern.compile(request.searchExpression()).asMatchPredicate();
            final var foundWords = selectedDictionary.words().stream().filter(regex).toList();
            final var searchResult = new DictionarySearchResult(foundWords);

            presenter.presentDictionarySearchResult(searchResult);
        }
    }

}
