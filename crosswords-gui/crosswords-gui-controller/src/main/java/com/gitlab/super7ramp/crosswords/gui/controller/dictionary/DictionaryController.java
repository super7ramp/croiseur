package com.gitlab.super7ramp.crosswords.gui.controller.dictionary;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryService;
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
    private final DictionaryService dictionaryService;

    /** The worker executing the dictionary tasks. */
    private final Executor executor;

    /**
     * Constructs an instance.
     *
     * @param dictionaryServiceArg the "dictionary" use-cases
     * @param executorArg          the worker executing the dictionary tasks
     */
    public DictionaryController(final DictionaryService dictionaryServiceArg,
                                final Executor executorArg) {
        dictionaryService = dictionaryServiceArg;
        executor = executorArg;
    }

    /**
     * Lists the entries of the given dictionary.
     */
    public void listDictionaryEntries(final DictionaryViewModel dictionaryViewModel) {
        execute(new ListDictionaryEntriesTask(dictionaryViewModel, dictionaryService));
    }

    /**
     * Lists the available dictionaries.
     */
    public void listDictionaries() {
        execute(new ListDictionariesTask(dictionaryService));
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
