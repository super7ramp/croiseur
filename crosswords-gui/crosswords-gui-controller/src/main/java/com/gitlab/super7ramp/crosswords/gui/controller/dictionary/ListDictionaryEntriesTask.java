package com.gitlab.super7ramp.crosswords.gui.controller.dictionary;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryUsecase;
import com.gitlab.super7ramp.crosswords.api.dictionary.ListDictionaryEntriesRequest;
import com.gitlab.super7ramp.crosswords.gui.view.model.DictionaryViewModel;
import javafx.concurrent.Task;

/**
 * List dictionaries task.
 */
final class ListDictionaryEntriesTask extends Task<Void> {

    /** The "dictionary" usecase. */
    private final DictionaryUsecase dictionaryUsecase;

    /** The "list dictionary entries" request. */
    private final ListDictionaryEntriesRequest listDictionaryEntriesRequest;

    /**
     * Constructs an instance.
     *
     * @param dictionaryUsecaseArg the dictionary usecase
     */
    ListDictionaryEntriesTask(final DictionaryViewModel dictionaryViewModelArg,
                              final DictionaryUsecase dictionaryUsecaseArg) {
        listDictionaryEntriesRequest = new ListDictionaryEntriesRequestImpl(dictionaryViewModelArg);
        dictionaryUsecase = dictionaryUsecaseArg;
    }

    @Override
    protected Void call() {
        dictionaryUsecase.listEntries(listDictionaryEntriesRequest);
        return null;
    }

}