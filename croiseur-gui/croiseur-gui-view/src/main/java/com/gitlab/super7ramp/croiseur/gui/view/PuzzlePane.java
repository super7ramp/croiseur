/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

import java.util.ResourceBundle;

/**
 * A pane to edit puzzle details.
 */
public final class PuzzlePane extends Accordion {

    /**
     * Constructs an instance.
     */
    public PuzzlePane() {
        FxmlLoaderHelper.load(this, ResourceBundle.getBundle(getClass().getName()));
    }

    @FXML
    private void initialize() {
        initializeTitledPanes();
    }

    /**
     * Initializes titled panes: Make sure always one titled pane is expanded.
     */
    private void initializeTitledPanes() {
        expandedPaneProperty().addListener((observable, oldValue, newValue) -> {
            final boolean hasExpanded = getPanes().stream().anyMatch(TitledPane::isExpanded);
            if (!hasExpanded && oldValue != null) {
                Platform.runLater(() -> setExpandedPane(oldValue));
            }
        });
        setExpandedPane(getPanes().get(0));
    }
}
