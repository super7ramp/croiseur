/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.controller.dictionary;

import re.belv.croiseur.api.dictionary.DictionaryIdentifier;
import re.belv.croiseur.api.dictionary.ListDictionaryEntriesRequest;
import re.belv.croiseur.gui.view.model.DictionaryViewModel;

/**
 * Implementation of {@link ListDictionaryEntriesRequest}.
 */
final class ListDictionaryEntriesRequestImpl implements ListDictionaryEntriesRequest {

    /** The dictionary identifier. */
    private final DictionaryIdentifier dictionaryId;

    /**
     * Constructs an instance.
     *
     * @param dictionaryViewModelArg the dictionary view model
     */
    ListDictionaryEntriesRequestImpl(final DictionaryViewModel dictionaryViewModelArg) {
        dictionaryId = new DictionaryIdentifier(dictionaryViewModelArg.provider(),
                dictionaryViewModelArg.name());
    }

    @Override
    public DictionaryIdentifier dictionaryIdentifier() {
        return dictionaryId;
    }
}
