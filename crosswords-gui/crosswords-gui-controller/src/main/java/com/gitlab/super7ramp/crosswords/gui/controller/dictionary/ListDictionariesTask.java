/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.gui.controller.dictionary;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionariesRequest;
import javafx.concurrent.Task;

/**
 * List dictionaries task.
 */
final class ListDictionariesTask extends Task<Void> {

    /** The "dictionary" usecase. */
    private final DictionaryService dictionaryService;

    /** The "list dictionaries" request. */
    private final ListDictionariesRequest listDictionariesRequest;

    /**
     * Constructs an instance.
     *
     * @param dictionaryServiceArg the dictionary usecase
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
