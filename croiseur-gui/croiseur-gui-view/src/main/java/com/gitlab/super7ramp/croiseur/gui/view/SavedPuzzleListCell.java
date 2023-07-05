/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.gui.view.model.SavedPuzzleViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;

/**
 * A specialized {@link ListCell} for displaying saved puzzles.
 */
public final class SavedPuzzleListCell extends ListCell<SavedPuzzleViewModel> {

    /** The card displaying puzzle information. */
    @FXML
    private SavedPuzzleCard card;

    /**
     * Constructs an instance.
     */
    public SavedPuzzleListCell() {
        FxmlLoaderHelper.load(this);
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
            setGraphic(card);
            setText(null);
        }
    }

}
