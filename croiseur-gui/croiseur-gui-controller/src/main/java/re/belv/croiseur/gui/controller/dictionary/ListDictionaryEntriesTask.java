/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.controller.dictionary;

import javafx.concurrent.Task;
import re.belv.croiseur.api.dictionary.DictionaryService;
import re.belv.croiseur.api.dictionary.ListDictionaryEntriesRequest;
import re.belv.croiseur.gui.view.model.DictionaryViewModel;

/**
 * List dictionaries task.
 */
final class ListDictionaryEntriesTask extends Task<Void> {

    /** The dictionary service. */
    private final DictionaryService dictionaryService;

    /** The "list dictionary entries" request. */
    private final ListDictionaryEntriesRequest listDictionaryEntriesRequest;

    /**
     * Constructs an instance.
     *
     * @param dictionaryViewModelArg the dictionary view model
     * @param dictionaryServiceArg   the dictionary service
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
