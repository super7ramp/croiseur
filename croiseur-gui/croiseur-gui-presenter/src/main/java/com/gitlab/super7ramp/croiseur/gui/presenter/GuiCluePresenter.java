/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.presenter;

import com.gitlab.super7ramp.croiseur.gui.view.model.CluesViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordGridViewModel;
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

    /** The crossword grid view model. */
    private final CrosswordGridViewModel gridViewModel;

    /** The errors view model. */
    private final ErrorsViewModel errorsViewModel;

    /**
     * Constructs an instance.
     *
     * @param cluesViewModelArg  the clues view model
     * @param gridViewModelArg   the crossword grid view model
     * @param errorsViewModelArg the errors view model
     */
    GuiCluePresenter(final CluesViewModel cluesViewModelArg,
                     final CrosswordGridViewModel gridViewModelArg,
                     final ErrorsViewModel errorsViewModelArg) {
        cluesViewModel = cluesViewModelArg;
        gridViewModel = gridViewModelArg;
        errorsViewModel = errorsViewModelArg;
    }

    @Override
    public void presentClueError(final String error) {
        LOGGER.warning(() -> "Received clue error: " + error);
        Platform.runLater(() -> errorsViewModel.addError(error));
    }

    @Override
    public void presentClueProviders(final List<ClueProviderDescription> clueProviderDescriptions) {
        LOGGER.info(() -> "Received clue providers: " + clueProviderDescriptions);
        final List<String> clueProviderNames =
                clueProviderDescriptions.stream().map(ClueProviderDescription::name).toList();
        Platform.runLater(() -> cluesViewModel.clueProvidersProperty().setAll(clueProviderNames));
    }

    @Override
    public void presentClues(final Map<String, String> clues) {
        LOGGER.info(() -> "Received clues: " + clues);
        Platform.runLater(() -> fillCluesViewModel(clues));
    }

    /**
     * Fills the clues view model with the given clues
     *
     * @param clues the clues, indexed by the defined words
     */
    private void fillCluesViewModel(final Map<String, String> clues) {
        final List<String> acrossSlotContents = gridViewModel.longAcrossSlotContents();
        for (int i = 0; i < acrossSlotContents.size(); i++) {
            final String word = acrossSlotContents.get(i);
            final String clue = clues.get(word);
            if (clue != null) {
                cluesViewModel.acrossClue(i).systemContent(clue);
            }
        }
        final List<String> downSlotContents = gridViewModel.longDownSlotContents();
        for (int i = 0; i < downSlotContents.size(); i++) {
            final String word = downSlotContents.get(i);
            final String clue = clues.get(word);
            if (clue != null) {
                cluesViewModel.downClue(i).systemContent(clue);
            }
        }
    }
}
