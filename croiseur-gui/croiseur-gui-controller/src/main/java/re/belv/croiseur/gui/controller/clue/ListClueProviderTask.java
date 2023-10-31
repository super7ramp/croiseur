/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.controller.clue;

import javafx.concurrent.Task;
import re.belv.croiseur.api.clue.ClueService;
import re.belv.croiseur.gui.view.model.CluesViewModel;

/**
 * Task to asynchronously list the clue providers.
 */
final class ListClueProviderTask extends Task<Void> {

    /** The clue service to call. */
    private final ClueService clueService;

    /**
     * Constructs an instance.
     *
     * @param cluesViewModel the clues view model
     * @param clueServiceArg the clue service
     */
    ListClueProviderTask(final CluesViewModel cluesViewModel, final ClueService clueServiceArg) {
        clueService = clueServiceArg;
        runningProperty().addListener(
                (observable, oldValue, newValue) -> cluesViewModel.clueServiceIsRunning(newValue));
    }

    @Override
    protected Void call() {
        clueService.listProviders();
        return null;
    }
}
