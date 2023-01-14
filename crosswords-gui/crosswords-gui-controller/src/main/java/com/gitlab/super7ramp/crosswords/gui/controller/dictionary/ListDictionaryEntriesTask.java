/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.gui.controller.dictionary;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionaryEntriesRequest;
import com.gitlab.super7ramp.crosswords.gui.view.model.DictionaryViewModel;
import javafx.concurrent.Task;

/**
 * List dictionaries task.
 */
final class ListDictionaryEntriesTask extends Task<Void> {

    /** The "dictionary" service. */
    private final DictionaryService dictionaryService;

    /** The "list dictionary entries" request. */
    private final ListDictionaryEntriesRequest listDictionaryEntriesRequest;

    /**
     * Constructs an instance.
     *
     * @param dictionaryServiceArg the dictionary service
     */
    ListDictionaryEntriesTask(final DictionaryViewModel dictionaryViewModelArg,
                              final DictionaryService dictionaryServiceArg) {
        listDictionaryEntriesRequest = new ListDictionaryEntriesRequestImpl(dictionaryViewModelArg);
        dictionaryService = dictionaryServiceArg;
    }

    @Override
    protected Void call() {
        dictionaryService.listEntries(listDictionaryEntriesRequest);
        return null;
    }

}
