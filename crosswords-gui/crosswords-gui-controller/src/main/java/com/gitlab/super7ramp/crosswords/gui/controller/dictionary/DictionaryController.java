package com.gitlab.super7ramp.crosswords.gui.controller.dictionary;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryUsecase;
import com.gitlab.super7ramp.crosswords.gui.view.model.DictionaryViewModel;
import javafx.concurrent.Task;

import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controls the dictionary.
 */
public final class DictionaryController {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(DictionaryController.class.getName());

    /** The dictionary use-cases. */
    private final DictionaryUsecase usecase;

    /** The worker executing the dictionary tasks. */
    private final Executor executor;

    /**
     * Constructs an instance.
     *
     * @param usecaseArg  the "dictionary" use-cases
     * @param executorArg the worker executing the dictionary tasks
     */
    public DictionaryController(final DictionaryUsecase usecaseArg,
                                final Executor executorArg) {
        usecase = usecaseArg;
        executor = executorArg;
    }

    /**
     * Lists the entries of the given dictionary.
     */
    public void listDictionaryEntries(final DictionaryViewModel dictionaryViewModel) {
        execute(new ListDictionaryEntriesTask(dictionaryViewModel, usecase));
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
    private void execute(final Task<Void> task) {
        task.setOnFailed(event -> LOGGER.log(Level.WARNING, "Dictionary task failed.",
                task.getException()));
        executor.execute(task);
    }

}
