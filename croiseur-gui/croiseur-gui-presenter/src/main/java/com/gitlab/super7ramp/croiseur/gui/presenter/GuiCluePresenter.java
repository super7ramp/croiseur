/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.presenter;

import com.gitlab.super7ramp.croiseur.gui.view.model.CluesViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.ErrorsViewModel;
import com.gitlab.super7ramp.croiseur.spi.presenter.clue.CluePresenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.clue.ClueProviderDescription;
import javafx.application.Platform;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * GUI implementation of {@link CluePresenter}
 */
final class GuiCluePresenter implements CluePresenter {

    /** The logger. */
    private static final Logger LOGGER = Logger.getLogger(GuiCluePresenter.class.getName());

    /** The clues view model. */
    private final CluesViewModel cluesViewModel;

    /** The errors view model. */
    private final ErrorsViewModel errorsViewModel;

    /**
     * Constructs an instance.
     *
     * @param cluesViewModelArg  the clues view model
     * @param errorsViewModelArg the errors view model
     */
    GuiCluePresenter(final CluesViewModel cluesViewModelArg,
                     final ErrorsViewModel errorsViewModelArg) {
        cluesViewModel = cluesViewModelArg;
        errorsViewModel = errorsViewModelArg;
    }

    @Override
    public void presentClueError(final String error) {
        LOGGER.warning(() -> "Received clue error: " + error);
        Platform.runLater(() -> errorsViewModel.addError(error));
    }

    @Override
    public void presentClueProviders(final List<ClueProviderDescription> clueProviderDescriptions) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentClues(final Map<String, String> clues) {
        LOGGER.info(() -> "Received clues: " + clues);
        Platform.runLater(() -> fillCluesViewModel(clues));
    }

    private void fillCluesViewModel(final Map<String, String> clues) {
        if (clues.isEmpty()) {
            errorsViewModel.addError("No clue received from clue provider");
            return;
        }
        // GUI fills clue one by one, there shouldn't be multiple clues
        if (clues.size() > 1) {
            errorsViewModel.addError("Inconsistent number of clues received from clue provider");
            return;
        }
        final String clue = clues.values().iterator().next();
        final int selectedAcrossClue = cluesViewModel.selectedAcrossClueIndex();
        final int selectedDownClue = cluesViewModel.selectedDownClueIndex();
        if (selectedAcrossClue != -1) {
            cluesViewModel.acrossClue(selectedAcrossClue).systemContent(clue);
        } else if (selectedDownClue != -1){
            cluesViewModel.downClue(selectedDownClue).systemContent(clue);
        } else {
            errorsViewModel.addError("Received clue from clue provider, but no slot selected");
        }
    }
}
