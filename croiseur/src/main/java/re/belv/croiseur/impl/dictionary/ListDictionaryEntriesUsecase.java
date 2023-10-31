/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.dictionary;

import re.belv.croiseur.api.dictionary.ListDictionaryEntriesRequest;
import re.belv.croiseur.common.dictionary.ProvidedDictionaryDetails;
import re.belv.croiseur.common.util.Either;
import re.belv.croiseur.impl.dictionary.selection.DictionarySelector;
import re.belv.croiseur.impl.dictionary.selection.SelectedDictionary;
import re.belv.croiseur.spi.presenter.dictionary.DictionaryContent;
import re.belv.croiseur.spi.presenter.dictionary.DictionaryPresenter;

import java.util.Set;

/**
 * Lists the dictionary entries.
 */
final class ListDictionaryEntriesUsecase {

    /** The dictionary selector. */
    private final DictionarySelector dictionarySelector;

    /** The presenter. */
    private final DictionaryPresenter presenter;

    /**
     * Constructs an instance.
     *
     * @param dictionarySelectorArg the dictionary selector
     * @param presenterArg          the presenter
     */
    ListDictionaryEntriesUsecase(final DictionarySelector dictionarySelectorArg,
                                 final DictionaryPresenter presenterArg) {
        dictionarySelector = dictionarySelectorArg;
        presenter = presenterArg;
    }

    /**
     * Reads the content of the given dictionary into a presentable {@link DictionaryContent}.
     *
     * @param selectedDictionary the selected dictionary
     * @return the presentable content of the given dictionary
     */
    private static DictionaryContent readContent(final SelectedDictionary selectedDictionary) {
        final ProvidedDictionaryDetails details = selectedDictionary.details();
        final Set<String> words = selectedDictionary.words(); // actually read content here
        return new DictionaryContent(details, words);
    }

    /**
     * Processes the given {@link ListDictionaryEntriesRequest}.
     *
     * @param request the {@link ListDictionaryEntriesRequest}
     */
    void process(final ListDictionaryEntriesRequest request) {
        final Either<String, SelectedDictionary> dictionarySelection =
                dictionarySelector.select(request.dictionaryIdentifier());
        if (dictionarySelection.isRight()) {
            final DictionaryContent dictionaryContent =
                    readContent(dictionarySelection.right());
            presenter.presentDictionaryEntries(dictionaryContent);
        } else {
            presenter.presentDictionaryError(dictionarySelection.left());
        }
    }
}
