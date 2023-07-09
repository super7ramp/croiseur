/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.gui.view.model.SavedPuzzleViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.util.ResourceBundle;

/**
 * A specialized {@link ListCell} for displaying saved puzzles.
 */
public final class SavedPuzzleListCell extends ListCell<SavedPuzzleViewModel> {

    /** The graphic container, when element is present. */
    @FXML
    private HBox containerHBox;

    /** The card displaying puzzle information, child of {@link #containerHBox}. */
    @FXML
    private SavedPuzzleCard card;

    /**
     * Constructs an instance.
     */
    public SavedPuzzleListCell() {
        FxmlLoaderHelper.load(this, ResourceBundle.getBundle(getClass().getName()));
    }

    @Override
    protected void updateItem(final SavedPuzzleViewModel item, final boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
            card.reset();
        } else {
            card.set(item);
            setGraphic(containerHBox);
            setText(null);
        }
    }

}
