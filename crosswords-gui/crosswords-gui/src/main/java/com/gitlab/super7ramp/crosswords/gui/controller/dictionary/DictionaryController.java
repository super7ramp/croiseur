package com.gitlab.super7ramp.crosswords.gui.controller.dictionary;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryUsecase;

import java.util.concurrent.Executor;

/**
 * Controls the dictionary.
 */
public final class DictionaryController {

    /** The worker executing the dictionary tasks. */
    private final Executor executor;

    /** The dictionary use-cases. */
    private final DictionaryUsecase usecase;

    /**
     * Constructs an instance.
     *
     * @param usecaseArg the "dictionary" use-cases
     */
    public DictionaryController(final DictionaryUsecase usecaseArg, final Executor executorArg) {
        usecase = usecaseArg;
        executor = executorArg;
    }

    /**
     * Lists the entries of the selected dictionary.
     */
    public void listDictionaryEntries() {
        execute(new ListDictionaryEntriesTask(usecase));
    }

    /**
     * Lists the available dictionaries.
     */
    public void listDictionaries() {
        execute(new ListDictionariesTask(usecase));
    }

    /**
     * Executes the given task.
     *
     * @param task the task to execute
     */
    private void execute(final Runnable task) {
        executor.execute(task);
    }

}
