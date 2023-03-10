/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.dictionary;

import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.croiseur.api.dictionary.ListDictionaryEntriesRequest;
import com.gitlab.super7ramp.croiseur.gui.view.model.DictionaryViewModel;

/**
 * Implementation of {@link ListDictionaryEntriesRequest}.
 */
final class ListDictionaryEntriesRequestImpl implements ListDictionaryEntriesRequest {

    /** The dictionary identifier. */
    private final DictionaryIdentifier dictionaryId;

    /**
     * Constructs an instance.
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
