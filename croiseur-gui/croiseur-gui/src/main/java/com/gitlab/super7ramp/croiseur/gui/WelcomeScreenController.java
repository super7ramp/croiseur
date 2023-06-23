/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui;

import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.gui.view.WelcomeScreen;
import javafx.fxml.FXML;

/**
 * The welcome screen controller.
 */
public class WelcomeScreenController {

    /** The welcome screen view. */
    @FXML
    private WelcomeScreen view;

    /**
     * Constructs an instance.
     */
    public WelcomeScreenController() {
        // Nothing to do.
    }

    @FXML
    private void initialize() {
        view.onNewPuzzleButtonActionProperty().set(e -> onNewPuzzleButtonAction());
        view.onOpenPuzzleButtonActionProperty().set(e -> onOpenPuzzleButtonAction());
    }

    private void onNewPuzzleButtonAction() {
        // TODO switch scene
    }

    private void onOpenPuzzleButtonAction() {
        final SavedPuzzle puzzleToOpen = view.selectedPuzzleProperty().getValue();
        // TODO switch scene, pass the puzzle to open
    }
}
