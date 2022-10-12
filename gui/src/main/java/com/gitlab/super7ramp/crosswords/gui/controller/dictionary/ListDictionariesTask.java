package com.gitlab.super7ramp.crosswords.gui.controller.dictionary;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryUsecase;
import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionariesRequest;
import javafx.concurrent.Task;

/**
 * List dictionaries task.
 */
final class ListDictionariesTask extends Task<Void> {

    /** The "dictionary" usecase. */
    private final DictionaryUsecase dictionaryUsecase;

    /** The "list dictionaries" request. */
    private final ListDictionariesRequest listDictionariesRequest;

    /**
     * Constructs an instance.
     *
     * @param dictionaryUsecaseArg the dictionary usecase
     */
    ListDictionariesTask(final DictionaryUsecase dictionaryUsecaseArg) {
        listDictionariesRequest = new ListDictionaryRequestImpl();
        dictionaryUsecase = dictionaryUsecaseArg;
    }

    @Override
    protected Void call() {
        dictionaryUsecase.listDictionaries(listDictionariesRequest);
        return null;
    }

}
