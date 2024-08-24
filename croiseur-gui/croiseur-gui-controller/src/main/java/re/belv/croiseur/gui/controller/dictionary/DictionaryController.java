/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.controller.dictionary;

import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import re.belv.croiseur.api.dictionary.DictionaryService;
import re.belv.croiseur.gui.view.model.DictionaryViewModel;

/**
 * Controls calls to the dictionary service.
 */
public final class DictionaryController {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(DictionaryController.class.getName());

    /** The dictionary service. */
    private final DictionaryService dictionaryService;

    /** The worker executing the dictionary tasks. */
    private final Executor executor;

    /**
     * Constructs an instance.
     *
     * @param dictionaryServiceArg the dictionary service
     * @param executorArg          the worker executing the dictionary tasks
     */
    public DictionaryController(final DictionaryService dictionaryServiceArg, final Executor executorArg) {
        dictionaryService = dictionaryServiceArg;
        executor = executorArg;
    }

    /**
     * Lists the entries of the given dictionary.
     *
     * @param dictionaryViewModel the dictionary view-model
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
        task.setOnFailed(event -> LOGGER.log(Level.WARNING, "Dictionary task failed.", task.getException()));
        executor.execute(task);
    }
}
