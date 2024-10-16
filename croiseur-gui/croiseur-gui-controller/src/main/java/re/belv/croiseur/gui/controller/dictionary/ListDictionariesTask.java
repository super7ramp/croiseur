/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.controller.dictionary;

import javafx.concurrent.Task;
import re.belv.croiseur.api.dictionary.DictionaryService;
import re.belv.croiseur.api.dictionary.ListDictionariesRequest;

/** List dictionaries task. */
final class ListDictionariesTask extends Task<Void> {

    /** The dictionary service. */
    private final DictionaryService dictionaryService;

    /** The "list dictionaries" request. */
    private final ListDictionariesRequest listDictionariesRequest;

    /**
     * Constructs an instance.
     *
     * @param dictionaryServiceArg the dictionary service
     */
    ListDictionariesTask(final DictionaryService dictionaryServiceArg) {
        listDictionariesRequest = new ListDictionaryRequestImpl();
        dictionaryService = dictionaryServiceArg;
    }

    @Override
    protected Void call() {
        dictionaryService.listDictionaries(listDictionariesRequest);
        return null;
    }
}
