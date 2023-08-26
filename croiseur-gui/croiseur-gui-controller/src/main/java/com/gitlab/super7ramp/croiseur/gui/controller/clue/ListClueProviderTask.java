/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.clue;

import com.gitlab.super7ramp.croiseur.api.clue.ClueService;
import com.gitlab.super7ramp.croiseur.gui.view.model.clue.CluesViewModel;
import javafx.concurrent.Task;

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
