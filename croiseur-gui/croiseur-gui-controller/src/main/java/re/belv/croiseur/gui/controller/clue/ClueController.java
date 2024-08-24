/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.controller.clue;

import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import re.belv.croiseur.api.clue.ClueService;
import re.belv.croiseur.gui.view.model.CluesViewModel;
import re.belv.croiseur.gui.view.model.CrosswordGridViewModel;

/** Controls calls to the clue service. */
public final class ClueController {

    /** The logger. */
    private static final Logger LOGGER = Logger.getLogger(ClueController.class.getName());

    /** The clues view model. */
    private final CluesViewModel cluesViewModel;

    /** The crossword grid view model. */
    private final CrosswordGridViewModel crosswordGridViewModel;

    /** The clue service to call. */
    private final ClueService clueService;

    /** The worker executing the clue tasks. */
    private final Executor executor;

    /**
     * Constructs an instance.
     *
     * @param cluesViewModelArg the clues view model
     * @param crosswordGridViewModelArg the crossword grid view model
     * @param clueServiceArg the clue service to call
     * @param executorArg the worker executing the clue tasks.
     */
    public ClueController(
            final CluesViewModel cluesViewModelArg,
            final CrosswordGridViewModel crosswordGridViewModelArg,
            final ClueService clueServiceArg,
            final Executor executorArg) {
        cluesViewModel = cluesViewModelArg;
        crosswordGridViewModel = crosswordGridViewModelArg;
        clueService = clueServiceArg;
        executor = executorArg;
    }

    /** Lists the available clue providers. */
    public void listClueProviders() {
        execute(new ListClueProviderTask(cluesViewModel, clueService));
    }

    /** Gets the clue for the currently selected slot. */
    public void getClueForCurrentSlot() {
        execute(new GetClueTask(cluesViewModel, crosswordGridViewModel, clueService));
    }

    /**
     * Executes the given task.
     *
     * @param task the task to execute
     */
    private void execute(final Task<Void> task) {
        task.setOnFailed(event -> LOGGER.log(Level.WARNING, "Clue task failed.", task.getException()));
        executor.execute(task);
    }
}
