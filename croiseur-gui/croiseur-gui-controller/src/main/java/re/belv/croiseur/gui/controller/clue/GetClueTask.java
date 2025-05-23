/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.controller.clue;

import javafx.concurrent.Task;
import re.belv.croiseur.api.clue.ClueService;
import re.belv.croiseur.api.clue.GetClueRequest;
import re.belv.croiseur.gui.view.model.CluesViewModel;
import re.belv.croiseur.gui.view.model.CrosswordGridViewModel;

/** Task to asynchronously generate a clue for the current slot. */
final class GetClueTask extends Task<Void> {

    /** The service to call. */
    private final ClueService clueService;

    /** The request to pass to the service. */
    private final GetClueRequest request;

    /**
     * Constructs an instance.
     *
     * @param cluesViewModel the clues view model
     * @param crosswordGridViewModel the crossword grid view model
     * @param clueServiceArg the service to call
     */
    public GetClueTask(
            final CluesViewModel cluesViewModel,
            final CrosswordGridViewModel crosswordGridViewModel,
            final ClueService clueServiceArg) {
        request = new GetClueRequestImpl(crosswordGridViewModel);
        clueService = clueServiceArg;
        runningProperty()
                .addListener((observable, oldValue, newValue) -> cluesViewModel.clueServiceIsRunning(newValue));
    }

    @Override
    protected Void call() {
        clueService.getClues(request);
        return null;
    }
}
